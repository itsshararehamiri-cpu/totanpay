package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.TopupTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import com.google.gson.Gson


class TopupTransaction(
    request: TopupTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?
    ) -> Unit,
    sendTransactionInQueue: suspend () -> Boolean,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit) : BaseTransaction(
    request,
    iConnection,
    saveReverseData,
    saveTransactionLog,
    sendTransactionInQueue,
    updateTransaction
) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.TOPUP.tag
    override val needReport: Boolean
        get() = true

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0200"
            processCode = "220000"
            stan = (request as TopupTransactionRequest).stan.toString()
           //pan = request.pan
            amount = request.amount
            setTrack2(request.track2)
            setCurrency(request.currency)
            setPOS(request.POS)
            setPinBlock(request.pinBlock)
            setMerchantId(request.merchantId)
            setDateTime(request.date, request.time)
            setNii(request.nii)
            set(25,"00")
            setTerminalId(request.terminalId!!)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setProductCode(request.productCode)//"1142"
                setMobileNumber(request.mobile)
               //setTerminalLanguage(request.terminalLanguage)
//                setTerminalConnectionType(request.terminalConnectionType)
            }
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        updateTransaction(sendMessage.tranDate,sendMessage.tranTime, (request as TopupTransactionRequest).pan,
            cardIssuer,            receivedIsoMessage.respCode.toString(),request.amount,receivedIsoMessage.rrn?:""
        ,null,null,null,request.mobile,101)// TODO:
            return BaseTransactionResponse.TopupTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            rrn = receivedIsoMessage.rrn ?: "",
            trace = sendMessage.stan,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            issuerName = cardIssuer,
            maskedPan = request.pan,
            amount = sendMessage.getString(4))
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        if (receivedIsoMessage == null) {
            updateTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime,
                (request as TopupTransactionRequest).pan,
                "",
                "-1",
                request.amount, null, null, null, null, null, null
            )
            val v= FaildTransactionResponse(
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan.toInt(),
                cardIssuer = "",
                maskedPan = sendMessage.pan,
                amount = request.amount
            )
            return v
        } else {
            if (receivedIsoMessage.respCode != 80)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            updateTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime,
                (request as TopupTransactionRequest).pan,
                cardIssuer,
                receivedIsoMessage.respCode.toString(),
                request.amount,
                receivedIsoMessage.rrn, null, null, null, null, null
            )
            return FaildTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan.toInt(),
                cardIssuer = cardIssuer,
                maskedPan = request.pan,
                amount = request.amount,
                rrn = receivedIsoMessage.rrn,
            )
        }

    }
}



