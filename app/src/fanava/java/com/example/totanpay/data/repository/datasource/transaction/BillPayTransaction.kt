package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BillPayTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import org.jpos.iso.ISOUtil

class BillPayTransaction(
    request: BillPayTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?,
        responseMessage: String?,trace:String?
    ) -> Unit,
    deleteTransaction: suspend (
        date: String, time: String
    ) -> Unit,
    sendTransactionInQueue: suspend () -> Boolean,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit,
    clearTransactionFromQueue: suspend (date: String, time: String) -> Unit
) : BaseTransaction(
    request,
    iConnection,
    saveReverseData,
    saveTransactionLog,
    sendTransactionInQueue,
    updateTransaction,
    deleteTransaction = deleteTransaction,
    clearTransactionFromQueue = clearTransactionFromQueue
) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.BILL_PAY.tag
    override val needReport: Boolean
        get() = true
    private var serviceDesc: String = ""
    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0200"
            processCode = "170000"
            amount = (request as BillPayTransactionRequest).amount
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setPOS(request.POS)
            setNii(request.nii)
            set(25, "00")
            setTrack2(request.track2)
            setTerminalId(request.terminalId)
            setMerchantId(request.merchantId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setBillId(request.billId)
                setPayId(request.payId)
                setTerminalConnectionType(request.terminalConnectionType)
            }
            setCurrency(request.currency)
            setPinBlock(request.pinBlock)
            serviceDesc = request.serviceDesc
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        val billName = receivedIsoMessage.getField48Tag(0x51)?.split("\\")?.get(0) ?: ""
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        var responseMessage = ""
        if (receivedIsoMessage.hasField(47)) {
            if (receivedIsoMessage.getBytes(47) != null) responseMessage =
                ISOUtil.hexString(
                    receivedIsoMessage.getBytes(
                        47
                    )
                )
        }
        var ss = receivedIsoMessage.getField48Tag(0x44)?.split("\\")?.get(0) ?: ""
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }

        updateTransaction(
            sendMessage.tranDate,
            sendMessage.tranTime,
            (request as BillPayTransactionRequest).pan,
            cardIssuer,
            receivedIsoMessage.getString(39),
            sendMessage.amount,
            receivedIsoMessage.rrn ?: "",
            billName,
            null, null, null, null, null,
            if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString(38)
            } else request.stan.toString()
        )

        return BaseTransactionResponse.BillPayTransactionResponse(
            responseCode = 0,
            responseMessage = responseMessage,
            reasonCode = null,
            rrn = receivedIsoMessage.rrn ?: "",
            trace = if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString(38)
            } else request.stan.toString(),
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            issuerName = cardIssuer,
            maskedPan = request.pan,
            amount = request.amount,
            posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",dateTimeOfServer = ltv.getNode(0x50)?.toString(),
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        if (receivedIsoMessage == null) {
            deleteTransaction(
                sendMessage.tranDate, sendMessage.tranTime
            )
            return FailedTransactionResponse(
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                amount = (request as BillPayTransactionRequest).amount,
                maskedPan = request.pan,
                posCode = null
            )
        } else {
            if (receivedIsoMessage.respCode != 80)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            deleteTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime
            )
            return FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                reasonCode = null,
                rrn = receivedIsoMessage.rrn ?: "",
                stan = receivedIsoMessage.stan.toString(),
                date = request.date,
                time = request.time,
                maskedPan = (request as BillPayTransactionRequest).pan,
                amount = request.amount,
                responseMessage = "",
                cardIssuer = cardIssuer,
                posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}
