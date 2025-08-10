package com.example.totanpay.data.repository.datasource.transaction


import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BalanceTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse

class BalanceTransaction(
    request: BalanceTransactionRequest,
    private val macGenerator: IMacGenerator,
    connection: IConnection
) : BaseTransaction(request, connection, {}, {}) {
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
            pan = (request as BalanceTransactionRequest).pan
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setPOS(request.POS)
            setNii(request.nii)
            setCurrency(request.currency)
            setTrack2(request.track2)
            setTerminalId(request.terminalId)
            setMerchantId(request.merchantId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType(request.terminalConnectionType)
            }
            setPinBlock(request.pinBlock)
            macGenerator.getMac(this)
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
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
        return BaseTransactionResponse.BalanceTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            balance = balance,
            currency = receivedIsoMessage.getString(49) ?: "",
            rrn = receivedIsoMessage.rrn ?: "",
            trace = if (receivedIsoMessage.hasField(38)) {
                receivedIsoMessage.getString(38)
                    .trim()
            } else {
                request.stan.toString()
            },
            availableBalance = availableBalance,
            issuerName = cardIssuer,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            maskedPan = sendMessage.pan.mask(),
            posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",dateTimeOfServer =ltv.getNode(0x50)?.toString(),
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
                stan = sendMessage.stan,
                maskedPan = sendMessage.pan.mask(),
                posCode = null
            )
        } else {
            FailedTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                responseMessage = "",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = if (receivedIsoMessage.hasField(38)) {
                    receivedIsoMessage.getString(38).trim()
                } else request.stan.toString(),
                maskedPan = sendMessage.pan.mask(),
                posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
            )
        }
    }
}

private fun parseField54(resp: String): Map<String, AmountStructure> {
    var count = 0
    var accountType: String
    var balancetype: String
    var currencyCode: String
    var amount: String
    var status: Char
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
