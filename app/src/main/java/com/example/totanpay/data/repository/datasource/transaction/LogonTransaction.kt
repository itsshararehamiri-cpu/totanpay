package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.util.toEnglishNumber

class LogonTransaction(
    request: LogonTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection
) : BaseTransaction(request, iConnection, {}, {}) {
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
            macGenerator.getMac(this)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        try {
            val field62 = receivedIsoMessage.getBytes(62)
            val pinKey: ByteArray? = field62.sliceArray(0..15)
            val macKey: ByteArray? = field62.sliceArray(16..31)
            val dataKey: ByteArray? = field62.sliceArray(32..47)
            val dateTimeOfServer = receivedIsoMessage.getField48Tag(0x50) ?: ""
            if (field62 != null && pinKey != null && macKey != null && dataKey != null && dateTimeOfServer.isNotEmpty() && receivedIsoMessage.getString(64)!=null)
            {
                return BaseTransactionResponse.LogonTransactionResponse(
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
            else {
               return FailedTransactionResponse(
                    responseCode = -1,
                    responseMessage = "خطا در فرمت اطلاعات",
                    reasonCode = null,
                    date = sendMessage.tranDate,
                    time = sendMessage.tranTime,
                    stan = sendMessage.stan,
                    posCode = null
                )
            }
        } catch (e: Exception) {
            Log.d("TAG", "onSuccess: ${e.cause}")
            Log.d("TAG", "onSuccess: ${e.localizedMessage}")

            return FailedTransactionResponse(
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
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
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = null
            )
        } else {
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan,
                posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}