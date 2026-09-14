package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BillInquiryTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse

class BillInquiryTransaction(
    request: BillInquiryTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    sendTransactionInQueue: suspend () -> Boolean
) : BaseTransaction(
    request,
    iConnection,
    {},
    {},
    sendTransactionInQueue
) {
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
            stan = (request as BillInquiryTransactionRequest).stan.toString()
            setDateTime(request.date, request.time)
            setNii(request.nii)
            set(25, "00")
            setTerminalId(request.terminalId)
            setMerchantId(request.merchantId)
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
        val field51=receivedIsoMessage.getField48Tag(0x51)?.split("\\")
        val serviceDesc: String =field51 ?.get(0) ?: ""
        val englishServiceDesc: String = if((field51?.size ?: 0) > 2)field51?.get(1) ?: "" else ""
        val billType = receivedIsoMessage.getField48Tag(0x68)?.split("\\")?.get(0) ?: ""
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
        return BaseTransactionResponse.BillInquiryTransactionResponse(
            billType = billType,
            serviceDesc = serviceDesc,
            englishServiceDesc=englishServiceDesc,
            amount = receivedIsoMessage.getString(4),
            responseCode = 0,
            responseMessage = R.string.empty_message,
            reasonCode = 0,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            trace = if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString("38").toString()
            } else request.stan.toString(),dateTimeOfServer = ltv.getNode(0x50),
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null) {
            FailedTransactionResponse(
                stan = sendMessage.stan,
                responseCode = -1,
                responseMessage = ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                maskedPan = "",
                amount = "", posCode = null
            )
        } else {
            val serviceDesc: String =
                receivedIsoMessage.getField48Tag(0x51)?.split("\\")?.get(0) ?: ""
            val billType = receivedIsoMessage.getField48Tag(0x68)?.split("\\")?.get(0) ?: ""
            var dateTimeOfServer: String? = null
            if (receivedIsoMessage.getBytes(48) != null) {
                val ltv: Ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
                dateTimeOfServer = ltv?.getNode(0x50)
            }
            return FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = null,
                reasonCode = 0,
                amount = "",
                date = sendMessage.tranDate,
                stan = if (receivedIsoMessage.hasField(38)) {
                    receivedIsoMessage.getString("38").trim()
                } else request.stan.toString(),
                time = sendMessage.tranTime, posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
                dateTimeOfServer = dateTimeOfServer
            )
        }
    }
}

