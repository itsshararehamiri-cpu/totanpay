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
    private val sendTransactionInQueue: suspend () -> Boolean ,
    protected val updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?, serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?, responseMessage: Int?, trace: String?
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
                    val m = IsoMessage()
                    m.set(39, ResponseMessageContainer.RC_5.code)
                    return m
                }
                if (isReversible) saveReverseData(sendMessage)
                if (needReport) saveTransactionLog(sendMessage)
                try {
                    conn.send(sendMessage)
                } catch (e: Exception) {
                    val m = IsoMessage()
                    m.set(39, ResponseMessageContainer.RC_6.code)
                    return m
                }
                val receiveMsg: IsoMessage? = conn.receive()
                if (receiveMsg?.hasField(48) == true) {
                    receiveMsg.setField48()
                }
                return receiveMsg
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun execute(): BaseTransactionResponse {
        buildMessage()
        if (!sendTransactionInQueue()) {
            return FailedTransactionResponse(
                responseCode =  ResponseMessageContainer.RC_2.code.toInt(),
                responseMessage =ResponseMessageContainer.RC_2.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = null
            )
        }
        val resp = send()
        return if (resp == null || resp.respCode != 0) {
            if (resp != null && resp.respCode ==  ResponseMessageContainer.RC_6.code.toInt()) {
                FailedTransactionResponse(
                    responseCode =  ResponseMessageContainer.RC_6.code.toInt(),
                    responseMessage = ResponseMessageContainer.RC_6.messageId,
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan,
                    posCode = null
                )
            } else if (resp != null && resp.respCode ==  ResponseMessageContainer.RC_5.code.toInt()) {
                FailedTransactionResponse(
                    responseCode =  ResponseMessageContainer.RC_5.code.toInt(),
                    responseMessage = ResponseMessageContainer.RC_5.messageId,
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