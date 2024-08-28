package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import org.jpos.iso.packager.ISO87BPackager

private const val TAG = "BaseTransaction"

abstract class BaseTransaction(
    protected val request: BaseTransactionRequest,
    private val iConnection: IConnection,
    private val saveReverseData: suspend (msg: IsoMessage) -> Unit,
    private val saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    private val sendTransactionInQueue: suspend () -> Boolean = { true },
    protected val updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?
    ) -> Unit = { date, time, pan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode ->
    },
    protected val clearTransactionFromQueue: suspend (date: String, time: String) -> Unit = { x, y -> }
) {
    protected abstract val isReversible: Boolean
    protected abstract val type: Int
    protected abstract val needReport: Boolean
    protected val sendMessage = IsoMessage()

    init {
        sendMessage.packager = ISO87BPackager()
    }

    protected abstract suspend fun buildMessage()
    protected suspend fun send(): IsoMessage? {
        try {
            iConnection.use { conn ->
                println("iConnectiont before start")
                conn.start()
                println("iConnectiont after start")
                if (isReversible)
                    saveReverseData(sendMessage)
                if (needReport)
                    saveTransactionLog(sendMessage)
                println("iConnectiont before send")

                conn.send(sendMessage)
                println("iConnectiont after send")

                Log.i("TranBase", "send: ${sendMessage.getDump()}")
                val receiveMsg = conn.receive()
                println("iConnectiont after rece")

                if (receiveMsg?.hasField(48) == true) {
                    receiveMsg.setField48()
                }
                Log.i("TranBase", "Receivee: ${receiveMsg?.getDump()}")
                return receiveMsg
            }
        } catch (e: Exception) {
            Log.i("TranBgggasevafa", "ggggg", e.cause)
            Log.d("TranBasevafa", e.message.toString())
            e.printStackTrace()
            return null
        }
    }

    suspend fun execute(): BaseTransactionResponse {
        buildMessage()
        if (isReversible)
        {
            if (!sendTransactionInQueue()) {
                return FaildTransactionResponse(
                    responseCode = -2,
                    responseMessage = "خطا در ارسال تراکنش تسویه-بازگشت",
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan.toInt())
            }
        }
        val resp = send()
        return if (resp == null || resp.respCode != 0) {
            onFail(resp)
        } else {
            onSuccess(resp)
        }
    }

    abstract suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse
    abstract suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse

}