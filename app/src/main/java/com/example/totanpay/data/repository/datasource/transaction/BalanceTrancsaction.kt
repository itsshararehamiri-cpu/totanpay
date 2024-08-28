package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BalanceTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import com.google.gson.Gson

class AmountStructure(
    val accountType: String,
    val currencyCode: String,
    val amount: String,
    val statuc: Char
)

enum class BalanceType(val label: String) {
    REAL_BALANCE("01"), AVAILABLE_BALANCE("02");

    companion object {
        fun valueOfLabel(label: String): BalanceType {
            var temp: BalanceType? = null
            for (e in BalanceType.values()) {
                if (e.label == label) {
                    temp = e
                }
            }
            return temp?:BalanceType.REAL_BALANCE
        }
    }
}
class BalanceTransaction(
    request: BalanceTransactionRequest, private val macGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
) : BaseTransaction(request, iConnection, saveReverseData, saveReverseData) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.BALANCE.tag
    override val needReport: Boolean
        get() = false

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0100"
            processCode = "310000"
            stan = request.stan.toString()
            pan = (request as BalanceTransactionRequest).pan
            setDateTime(request.date,request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType(request.terminalConnectionType)
                setTerminalType(request.terminalType)
            }
            setTrack2(request.track2)
            setPinBlock(request.pinBlock)
            setMerchantId(request.merchantId)
            setCurrency(request.currency)
            setPOS(request.POS)
            macGenerator?.getMac(this)
        }
    }


    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse.BalanceTransactionResponse {
        var balance = ""
        var availableBalance = ""
        if (receivedIsoMessage.hasField(54)) {
            val parsedField54 = parseField54(receivedIsoMessage.getString(54))
            if (parsedField54[BalanceType.REAL_BALANCE.label] != null)
                balance = parsedField54[BalanceType.REAL_BALANCE.label]!!.amount
            if (parsedField54[BalanceType.AVAILABLE_BALANCE.label] != null)
                availableBalance = parsedField54[BalanceType.AVAILABLE_BALANCE.label]!!.amount
        }
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        balance = balance.takeUnless { it.isEmpty() } ?: receivedIsoMessage.getString(4) ?: ""
        return BaseTransactionResponse.BalanceTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            balance = balance,
            currency = receivedIsoMessage.getString(49) ?: "",
            rrn = receivedIsoMessage.rrn ?: "",
            trace = receivedIsoMessage.stan,
            availableBalance = availableBalance,
            issuerName = cardIssuer,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            maskedPan = sendMessage.pan.mask()
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
      return  if(receivedIsoMessage==null)
        {
             FaildTransactionResponse(
                responseCode=-1, responseMessage = "خطا در دریافت اطلاعات", reasonCode=null,
                date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt(), maskedPan = sendMessage.pan.mask()
            )
        }
        else{
             FaildTransactionResponse(
                responseCode=receivedIsoMessage.respCode, responseMessage="", reasonCode=null,
                date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt(), maskedPan = sendMessage.pan.mask()
            )
        }
    }
}

private fun parseField54(resp: String): Map<String, AmountStructure> {

    var count = 0
    var accountType = ""
    var balancetype = ""
    var currencyCode = ""
    var amount = ""
    var status = ' '
    val temp: MutableMap<String, AmountStructure> = HashMap()
    while (count <= resp.length - 20) {
        accountType = resp.substring(count, count + 2)
        count += 2
        balancetype = resp.substring(count, count + 2)
        count += 2
        currencyCode = resp.substring(count, count + 3)
        count += 3
        status = resp[count]
        count++
        amount = resp.substring(count, count + 12)
        count += 12
        temp[balancetype] = AmountStructure(accountType, currencyCode, amount, status)
    }
    return temp
}
