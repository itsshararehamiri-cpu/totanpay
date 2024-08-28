package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BillInqueryTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse

class BillInqueryTransaction(
    request: BillInqueryTransactionRequest, private val iMacGenerator: IMacGenerator,
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
            processCode = "190000"
            stan = request.stan.toString()
            setMerchantId((request as BillInqueryTransactionRequest).merchantId)
            setDateTime(request.date,request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId!!)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setBillId(request.billId)
                setPayId(request.payId)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType(request.terminalConnectionType)
                setTerminalType(request.terminalType)
            }
            iMacGenerator?.getMac(this)
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
        return FaildTransactionResponse(
            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه-بازگشت", reasonCode=null,
            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt()
        )
    }
    /*
    class BillInqueryTranResp(
        respCode: Int,
        respMessage: String,
        reasonCode: Int,
        var billType: String,
        amount: String,
        var serviceDesc: String

    ) : TranResp(respCode, respMessage, reasonCode, "", "", "", "", "", amount) {

    }

     */

}

