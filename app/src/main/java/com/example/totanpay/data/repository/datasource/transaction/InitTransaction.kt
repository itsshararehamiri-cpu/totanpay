package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.InitTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse


class InitTransaction(
    request: InitTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.LOGON.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0100"
            processCode = "930000"
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId((request as InitTransactionRequest).terminalId!!)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
            }
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
        return BaseTransactionResponse.InitTransactionResponse(
            merchantPhone = ltv.getNode(0x34)!!.split("\\")[0].trim(),
            merchantId = receivedIsoMessage.getString("42"),
            merchantName = ltv.getNode(0x31)!!.split("\\")[0].trim().trim(),
            0,
            null,
            null,
            sendMessage.tranDate,
            sendMessage.tranTime,
            receivedIsoMessage.rrn ?: sendMessage.stan
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        return FaildTransactionResponse(
            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه-بازگشت", reasonCode=null,
            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt()
        )
    }

}