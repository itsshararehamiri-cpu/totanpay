package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.TopUpTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse

class TopUpTransaction(
    request: TopUpTransactionRequest, private val macGenerator: IMacGenerator?,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?, responseMessage: Int?, trace:String?
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
    updateTransaction,deleteTransaction,clearTransactionFromQueue
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
            amount = (request as TopUpTransactionRequest).amount
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setPOS(request.POS)
            setNii(request.nii)
            set(25, "00")
            setTrack2(request.track2)
            setTerminalId(request.terminalId!!)
            setMerchantId(request.merchantId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setProductCode(request.productCode)
                setMobileNumber(request.mobile)
                setTerminalLanguage(request.terminalLanguage)
                if (!request.logs.isNullOrEmpty())
                    set99(request.logs)
            }
            setCurrency(request.currency)
            setPinBlock(request.pinBlock)
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
            (request as TopUpTransactionRequest).pan,
            cardIssuer,
            receivedIsoMessage.respCode.toString(),
            request.amount,
            receivedIsoMessage.rrn ?: "",
            null,
            null,
            null,
            request.mobile,
            request.productCode?.toInt(),null, if (receivedIsoMessage.hasField(38)){
                receivedIsoMessage.getString(38)
                    .toString()
            } else {
                request.stan.toString()
            },
        )
        return BaseTransactionResponse.TopUpTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            rrn = receivedIsoMessage.rrn ?: "",
            trace =  if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString("38").toString()
            } else request.stan.toString(),
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            issuerName = cardIssuer,
            maskedPan = request.pan,
            amount = sendMessage.getString(4), posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            dateTimeOfServer = ltv.getNode(0x50)?.toString(),
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        if (receivedIsoMessage == null) {
            deleteTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime
            )
            return FailedTransactionResponse(
                responseCode = -1,
                responseMessage =ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = request.stan.toString(),
                cardIssuer = "",
                maskedPan =   (request as TopUpTransactionRequest).pan,
                amount = request.amount, posCode = null
            )
        } else {
            if (receivedIsoMessage.respCode != 80 && receivedIsoMessage.respCode > 0)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            deleteTransaction(
                sendMessage.tranDate,
                sendMessage.tranTime
            )
            var dateTimeOfServer: String? = null
            if (receivedIsoMessage.getBytes(48) != null) {
                val ltv: Ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
                dateTimeOfServer = ltv?.getNode(0x50)
            }
            return FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = null,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan =  if (receivedIsoMessage.hasField(38)) {
                    receivedIsoMessage.getString("38").trim()
                } else request.stan.toString(),
                cardIssuer = cardIssuer,
                maskedPan =   (request as TopUpTransactionRequest).pan,
                amount = request.amount,
                rrn = receivedIsoMessage.rrn,posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
                dateTimeOfServer = dateTimeOfServer
            )
        }
    }
}



