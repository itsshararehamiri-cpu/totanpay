package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BillInqueryTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse

class BillInqueryTransaction(
    request: BillInqueryTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    sendTransactionInQueue: suspend () -> Boolean
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData,sendTransactionInQueue) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.BILL_INQUERY.tag
    override val needReport: Boolean
        get() = false
    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0100"
           // pan="603799****2201"
            processCode = "190000"
            stan = (request as BillInqueryTransactionRequest).stan.toString()
            setMerchantId(request.merchantId)
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId!!)
            set(25,"00")
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setBillId(request.billId)
                setPayId(request.payId)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType(request.terminalConnectionType)
                setTerminalType(request.terminalType)
            }
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val serviceDesc:String =  receivedIsoMessage.getField48Tag(0x51)?.split("\\")?.get(0) ?: ""
        val billType =  receivedIsoMessage.getField48Tag(0x68)?.split("\\")?.get(0) ?: ""
        return BaseTransactionResponse.BillInqueryTransactionResponse(
            billType = billType,
            serviceDesc = serviceDesc,
            amount = receivedIsoMessage.getString(4),
            responseCode = 0,
            responseMessage = "",
            reasonCode = 0,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            trace = sendMessage.stan
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        return if (receivedIsoMessage == null) {
            FaildTransactionResponse(
                stan = sendMessage.stan.toInt(),
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                maskedPan = sendMessage.pan,
                amount = ""
            )
        } else {
            val serviceDesc: String =
                receivedIsoMessage.getField48Tag(0x51)?.split("\\")?.get(0) ?: ""
            val billType = receivedIsoMessage.getField48Tag(0x68)?.split("\\")?.get(0) ?: ""
            return FaildTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = 0,
                amount = "",
                date = sendMessage.tranDate,
                stan = sendMessage.stan.toInt(),
                time = sendMessage.tranTime,posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}

