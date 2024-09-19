package com.example.totanpay.data.repository.datasource.transaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.SettlementReverseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse

class SettlementReverseTransaction(
    request: SettlementReverseTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?,
        responseMessage:String?,trace:String?
    ) -> Unit,
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData,updateTransaction=updateTransaction) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.SETTLEMENT_REVERSE.tag
    override val needReport: Boolean
        get() = false
    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = if ((request as SettlementReverseTransactionRequest).status =='R') "0420" else "0220"
            processCode = request.processingCode
            amount = request.amount
            stan = request.stan.toString()
            tranDate = request.date
            tranTime = request.time
            setDateTime(request.date,request.time)
            setNii(request.nii)
            set(25,request.posConditionCode)
            setTerminalId(request.terminalId)
            setMerchantId(request.merchantId)
            setField48 {
                setTerminalConnectionType(request.terminalConnectionType)
            }
            setCurrency(request.currency)
            macGenerator?.getMac(this)
        }
    }
    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        return BaseTransactionResponse.SettlementReverseTransactionResponse(
            responseCode = 0, responseMessage = null, reasonCode = null, date =
            receivedIsoMessage.tranDate, time = receivedIsoMessage
                .tranTime, rrn = receivedIsoMessage.rrn ?: "", trace = ""
        )
    }
    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return FailedTransactionResponse(
            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه-بازگشت", reasonCode=null,
            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan, posCode = null
        )
    }
}
