package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse

class GetKeyTransaction(val compressedPublicKey:String,
                        val hashCode:String,
                        val dateTimeInGMT:String,
                        request: LogonTransactionRequest,
                        private val iMacGenerator: IMacGenerator,
                        iConnection: IConnection,
                        saveReverseData: suspend (msg: IsoMessage) -> Unit,
                        saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
                        updateTransaction: suspend (date: String, time: String,pan:String,cardIssuer:String,responseCode:String,amount:String) -> Unit,
                        ) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.LOGON.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0800"
            processCode = "600000"
            set(7,dateTimeInGMT)
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            //setNii(request.nii)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage("0")
            }
            set(60,compressedPublicKey)
            set(62,hashCode.toString())
            this.dump(System.out,">")
            //iMacGenerator?.getMac(this,true)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val field62 = receivedIsoMessage.getBytes(62)
        val pinKey = field62.sliceArray(0..15)
        val macKey = field62.sliceArray(16..31)
        val dataKey = field62.sliceArray(32..47)
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
            rrn = ""
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        return FaildTransactionResponse(
            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه-بازگشت", reasonCode=null,
            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt()
        )
    }
}