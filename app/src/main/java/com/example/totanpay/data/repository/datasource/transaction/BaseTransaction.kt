package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import org.jpos.iso.packager.ISO87BPackager


abstract class BaseTransaction(
    protected val request: BaseTransactionRequest,
    private val connection: IConnection,
    private val saveReverseData: suspend (msg: IsoMessage) -> Unit,
    private val saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    private val sendTransactionInQueue: suspend () -> Boolean = { true },
    protected val updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?, serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?, responseMessage: String?, trace: String?
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _, _, _, _, _ ->
    },
    protected val deleteTransaction: suspend (
        date: String, time: String
    ) -> Unit = { _, _ ->
    },
    protected val clearTransactionFromQueue: suspend (date: String, time: String) -> Unit = { _, _ -> }
) {
    protected abstract val isReversible: Boolean
    protected abstract val type: Int
    protected abstract val needReport: Boolean
    protected val sendMessage = IsoMessage()

    init {
        sendMessage.packager = ISO87BPackager()
    }

    protected abstract suspend fun buildMessage()
    private suspend fun send(): IsoMessage? {
        try {
            connection.use { conn ->
                try {
                    conn.start()
                } catch (e: Exception) {
                    Log.d("TAG", "cause: ${e.cause}")
                    Log.d("TAG", "message: ${e.message}")
                    val m = IsoMessage()
                    m.set(39, "-5")
                    return m
                }
                if (isReversible) saveReverseData(sendMessage)
                if (needReport) saveTransactionLog(sendMessage)
                try {
                    sendMessage.dump(System.out,"sssen?")
                    conn.send(sendMessage)
                } catch (e: Exception) {
                    Log.d("TAG", "send: ${e.cause}")
                    Log.d("TAG", "message: ${e.message}")
                    val m = IsoMessage()
                    m.set(39, "-6")
                    return m
                }
                val receiveMsg: IsoMessage? = conn.receive()
                if (receiveMsg?.hasField(48) == true) {
                    receiveMsg.setField48()
                }
                receiveMsg?.dump(System.out,"hhhh")
                return receiveMsg
            }
        } catch (e: Exception) {
            Log.i("cause bb", "cause", e.cause)
            Log.d("message", e.message.toString())
            e.printStackTrace()
            return null
        }
    }

    suspend fun execute(): BaseTransactionResponse {
        buildMessage()
        if (!sendTransactionInQueue()) {
            return FailedTransactionResponse(
                responseCode = -2,
                responseMessage = "خطا در ارسال تراکنش تسویه-بازگشت",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = null
            )
        }
        val resp = send()
        return if (resp == null || resp.respCode != 0) {
            if (resp != null && resp.respCode == -6) {
                FailedTransactionResponse(
                    responseCode = -6,
                    responseMessage = "خطا در ارسال تراکنش",
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan,
                    posCode = null
                )
            } else if (resp != null && resp.respCode == -5) {
                FailedTransactionResponse(
                    responseCode = -5,
                    responseMessage = "خطا در برقراری ارتباط",
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan,
                    posCode = null
                )
            } else {
                onFail(resp)
            }
        } else {
            onSuccess(resp)
        }
    }

    abstract suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse
    abstract suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse

}