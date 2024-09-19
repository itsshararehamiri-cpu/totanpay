package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.MasterKeyConfirmTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import org.jpos.iso.ISOUtil

class MasterKeyConfirmationTransaction(
    val dateTimeInGMT: String,
    request: MasterKeyConfirmTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.LOGON.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0820"
            processCode = "600000"
            set(7, dateTimeInGMT)
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setNii(request.nii)
            set(32,(request as MasterKeyConfirmTransactionRequest).acquiringInstitutionIdentificationCode)// TODO:
            setTerminalId((request as MasterKeyConfirmTransactionRequest).terminalId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalLanguage("2")

            }
            set(62, ISOUtil.hex2byte(request.hashedMasterToken))
            macGenerator.getMac(this)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        return BaseTransactionResponse.MasterKeyConfirmationTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            null,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            trace = "",
            rrn = ""
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null)
        {
            FailedTransactionResponse(
                responseCode = -1,
                responseMessage ="خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan, posCode = null
            )
        }
        else{
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage ="",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}