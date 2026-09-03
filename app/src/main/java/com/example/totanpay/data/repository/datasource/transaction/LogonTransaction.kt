package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.util.toEnglishNumber

class LogonTransaction(
    request: LogonTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection
) : BaseTransaction(request, iConnection, {}, {},{true}) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.LOGON.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0800".toEnglishNumber()
            processCode = "920000".toEnglishNumber()
            stan = request.stan.toString().toEnglishNumber()
            setDateTime(request.date.toEnglishNumber(), request.time.toEnglishNumber())
            setNii(request.nii.toEnglishNumber())
            set(25, "00".toEnglishNumber())
            setField48 {
                setSerial(request.serial.toEnglishNumber())
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage.toEnglishNumber())
                setTerminalConnectionType((request as LogonTransactionRequest).terminalConnectionType.toEnglishNumber())
            }
            if ((request as LogonTransactionRequest).enableMac)
                macGenerator.getMac(this)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        return try {
            val field62: ByteArray? = receivedIsoMessage.getBytes(62)
            val pinKey: ByteArray? = field62?.sliceArray(0..15)
            val macKey: ByteArray? = field62?.sliceArray(16..31)
            val dataKey: ByteArray? = field62?.sliceArray(32..47)
            val dateTimeOfServer = receivedIsoMessage.getField48Tag(0x50) ?: ""
            if (field62 != null && pinKey != null && macKey != null && dataKey != null && dateTimeOfServer.isNotEmpty()
            ) {
                if ((request as LogonTransactionRequest).enableMac) {
                    if (receivedIsoMessage.getString(
                            64
                        ) != null
                    ) {
                        BaseTransactionResponse.LogonTransactionResponse(
                            pinKey = pinKey,
                            dataKey = dataKey,
                            macKey = macKey,
                            terminalId = receivedIsoMessage.getString(41),
                            responseCode = 0,
                            responseMessage = null,
                            null,
                            date = sendMessage.tranDate,
                            time = sendMessage.tranTime,
                            trace = "",
                            rrn = "", dateTimeOfServer = dateTimeOfServer
                        )
                    } else FailedTransactionResponse(
                        responseCode = -3,
                        responseMessage = ResponseMessageContainer.RC_3.messageId,
                        reasonCode = null,
                        date = sendMessage.tranDate,
                        time = sendMessage.tranTime,
                        stan = sendMessage.stan,
                        posCode = null
                    )
                } else {
                    BaseTransactionResponse.LogonTransactionResponse(
                        pinKey = pinKey,
                        dataKey = dataKey,
                        macKey = macKey,
                        terminalId = receivedIsoMessage.getString(41),
                        responseCode = 0,
                        responseMessage = null,
                        null,
                        date = sendMessage.tranDate,
                        time = sendMessage.tranTime,
                        trace = "",
                        rrn = "", dateTimeOfServer = dateTimeOfServer
                    )
                }
            } else {
                FailedTransactionResponse(
                    responseCode = -3,
                    responseMessage = ResponseMessageContainer.RC_3.messageId,
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan,
                    posCode = null
                )
            }
        } catch (e: Exception) {
            FailedTransactionResponse(
                responseCode = -3,
                responseMessage = ResponseMessageContainer.RC_3.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = null
            )
        }
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null) {
            FailedTransactionResponse(
                responseCode = -1,
                responseMessage = ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = null
            )
        } else {
            var dateTimeOfServer: String? = null
            if (receivedIsoMessage.getBytes(48) != null) {
                val ltv: Ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
                dateTimeOfServer = ltv?.getNode(0x50)
            }
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = R.string.empty_message,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
                dateTimeOfServer = dateTimeOfServer
            )
        }
    }
}