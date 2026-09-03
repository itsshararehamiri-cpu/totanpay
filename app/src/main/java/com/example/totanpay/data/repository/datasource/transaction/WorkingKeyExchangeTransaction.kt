package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.WorkingKeyExchangeTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import org.jpos.iso.ISOUtil

class WorkingKeyExchangeTransaction(
    request: WorkingKeyExchangeTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData,{true}) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.LOGON.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0800"
            processCode = "610000"
            set(7, (request as WorkingKeyExchangeTransactionRequest).dateTimeInGMT)
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setNii(request.nii)
            set(32,"581672052")// TODO:
            setTerminalId(request.terminalId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType("2")
            }
            set(62,ISOUtil.hex2byte(request.masterToken))
            macGenerator.getMac(this)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val field62 = receivedIsoMessage.getBytes(62)
        val pinKey = field62.sliceArray(0..15)
        val dataKey = field62.sliceArray(16..31)
        val macKey = field62.sliceArray(32..47)
        val  macKey2=ISOUtil.hexString(macKey)
        return BaseTransactionResponse.WorkingKeyExchangeTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            null,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            trace = "",
            rrn = "", macKey=ISOUtil.hexString(macKey),pinKey=ISOUtil.hexString(pinKey),
            dataKey=ISOUtil.hexString(dataKey), terminalId = receivedIsoMessage.getString(41),
            merchantId = receivedIsoMessage.getString(4),
            acquiringInstitutionIdentificationCode=receivedIsoMessage.getString(32)
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null)
        {
            FailedTransactionResponse(
                responseCode = -1,
                responseMessage =ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan, posCode = null
            )
        }
        else{
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage =R.string.empty_message,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}