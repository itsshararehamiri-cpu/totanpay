package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.GetKeyTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import org.jpos.iso.ISOUtil

class GetKeyTransaction(
    request: GetKeyTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    val decryptKeys: suspend (String, String, String) -> Map<String, String>,
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.GETKE.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0800"
            processCode = "600000"
            set(7, (request as GetKeyTransactionRequest).dateTimeInGMT)
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType("2")
            }
            set(60, request.compressedPOSPublicKey)
            set(62, ISOUtil.hex2byte(request.hashedOtp))
            macGenerator.getMac(this, 2)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val NMPK_PUBLIC = receivedIsoMessage.getString(60)//mac
        val TK_PUBLIC = receivedIsoMessage.getString(61)//master
        val field62 = receivedIsoMessage.getBytes(62)


        val masterMacMap = decryptKeys(
           TK_PUBLIC,
            NMPK_PUBLIC,
           ISOUtil.hexString(field62)
        )

        return BaseTransactionResponse.GetKeyTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            null,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            trace = sendMessage.stan,
            rrn = receivedIsoMessage.rrn, decryptedToken = masterMacMap.get("decryptedToken"),
            masterKeySwitch = masterMacMap.get("master"), macKeySwitch = masterMacMap.get("mac"),
            acquiringInstitutionIdentificationCode = receivedIsoMessage.getString(32),
            terminalId = receivedIsoMessage.getString(41),
            merchantId=receivedIsoMessage.getString(42)
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null) {
            FailedTransactionResponse(
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan, posCode = null
            )
        } else {
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan, posCode = null
            )
        }
    }
}