package com.example.totanpay.data.repository.datasource.transaction


import android.util.Log
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.PurchaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse

class PurchaseTransaction(
    request: PurchaseTransactionRequest,
    private val macGenerator: IMacGenerator?,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    sendTransactionInQueue: suspend () -> Boolean,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?, responseMessage: Int?, trace: String?
    ) -> Unit,
    deleteTransaction: suspend (
        date: String, time: String
    ) -> Unit,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit,
    private val setStatusToReverse: suspend (date: String, time: String) -> Unit,
    clearTransactionFromQueue: suspend (date: String, time: String) -> Unit
) : BaseTransaction(
    request,
    iConnection,
    saveReverseData,
    saveTransactionLog,
    sendTransactionInQueue,
    updateTransaction = updateTransaction,
    deleteTransaction = deleteTransaction,
    clearTransactionFromQueue = clearTransactionFromQueue
) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.PURCHASE.tag
    override val needReport: Boolean
        get() = true

    override suspend fun buildMessage() {
        Log.d("TAG", "buildMessadge: dddd->${(request as PurchaseTransactionRequest).purchaseId}")

        with(sendMessage) {
            mti = "0200"
            processCode = "000000"
            amount = (request as PurchaseTransactionRequest).amount
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
                if (!request.purchaseId.isNullOrEmpty()) {
                    Log.d("TAG", "buildMessadge: dddd")
                    setPurchaseId(request.purchaseId)
                }
                setTerminalConnectionType(request.terminalConnectionType)
                if (!request.logs.isNullOrEmpty())
                    set99(request.logs)
            }
            tranTime = getString(12)
            tranDate = getString(13)
            setCurrency(request.currency)
            setPinBlock(request.pinBlock)
            if (!request.apportionments.isNullOrEmpty()) {
                val stringBuffer = StringBuffer()
                for ((idx, ap) in request.apportionments.withIndex()) {
                    if (idx > 0) stringBuffer.append(";")
                    stringBuffer.append(ap.IBAN)
                    stringBuffer.append(",")
                    stringBuffer.append(ap.amount)
                    stringBuffer.append(",")
                    if (idx == 0) {
                        stringBuffer.append("1")
                    } else {
                        stringBuffer.append("2")
                    }
                }
                set(59, stringBuffer.toString())
            }
            macGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        updateTransaction(
            sendMessage.tranDate,
            sendMessage.tranTime,
            (request as PurchaseTransactionRequest).pan,
            cardIssuer,
            receivedIsoMessage.respCode.toString(),
            request.amount,
            receivedIsoMessage.rrn ?: "",
            null,
            null, null,
            null,
            null,
            R.string.empty_message,
            if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString(38)
            } else
                request.stan.toString(),
        )
        return BaseTransactionResponse.PurchaseTransactionResponse(
            amount = sendMessage.getString(4),
            issuerName = cardIssuer,
            responseCode = receivedIsoMessage.respCode,
            responseMessage = R.string.empty_message,
            reasonCode = null,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            maskedPan = request.pan,
            trace =
                if (receivedIsoMessage.hasField(38)) {
                    receivedIsoMessage.getString(38)
                } else
                    request.stan.toString(),
            rrn = receivedIsoMessage.rrn,
            posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            purchaseId = request.purchaseId, dateTimeOfServer = ltv.getNode(0x50)
        )

    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        if (receivedIsoMessage == null) {
            deleteTransaction(
                request.date,
                request.time
            )
            return FailedTransactionResponse(
                stan = sendMessage.stan,
                responseCode = ResponseMessageContainer.RC_1.code.toInt(),
                responseMessage = ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                maskedPan = (request as PurchaseTransactionRequest).pan,
                amount = request.amount,
                posCode = null
            )

        } else {
            if (receivedIsoMessage.respCode != 80 && receivedIsoMessage.respCode > 0)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            deleteTransaction(
                request.date,
                request.time
            )
            var dateTimeOfServer: String? = null
            if (receivedIsoMessage.getBytes(48) != null) {
                val ltv: Ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
                dateTimeOfServer = ltv?.getNode(0x50)
            }
            return FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                reasonCode = null,
                rrn = receivedIsoMessage.rrn ?: "",
                stan = if (receivedIsoMessage.hasField(38)) receivedIsoMessage.stan else request.stan.toString(),
                date = request.date,
                time = request.time,
                maskedPan = (request as PurchaseTransactionRequest).pan,
                amount = request.amount,
                responseMessage = null,
                cardIssuer = cardIssuer,
                posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
                dateTimeOfServer = dateTimeOfServer
            )
        }
    }
}

