package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.convertString
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.InitTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.AccountMerchant
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse


class InitTransaction(
    request: InitTransactionRequest,
    private val macGenerator: IMacGenerator,
    iConnection: IConnection
) : BaseTransaction(request, iConnection, {}, {},{true}) {
    override val isReversible: Boolean
        get() = false
    override val type: Int
        get() = TransactionType.INIT.tag
    override val needReport: Boolean
        get() = false
    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0100"
            processCode = "930000"
            stan = request.stan.toString()
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId((request as InitTransactionRequest).terminalId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setTerminalLanguage(request.terminalLanguage)
                setTerminalConnectionType(request.terminalConnectionType)
            }
            macGenerator.getMac(this)
        }
    }
    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val ltv = Ltv().also { it.unpack(receivedIsoMessage.getBytes(48)) }
        val accountMerchants: MutableList<AccountMerchant> = mutableListOf()
        if (receivedIsoMessage.hasField(59)) {
            val field59: String? = receivedIsoMessage.getString(59)
            if (!field59.isNullOrEmpty()) {
                val accountList = ArrayList<Array<String>>()
                for (account in field59.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()) {
                    accountList.add(account.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray())
                }
                for (acc in accountList) {
                    val accountMerchant = AccountMerchant()
                    if (acc.size == 3) {
                        if (acc[2].isNotEmpty()) {
                            val bn = acc[2].split("\\\\".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                            if (bn.size >= 2) {
                                accountMerchant.englishBankName = bn[1]
                                accountMerchant.farsiBankName = convertString(bn[0])

                            } else {
                                accountMerchant.farsiBankName = convertString(bn[2])
                            }
                        }
                        accountMerchant.number = (acc[0])
                        accountMerchant.isActive = true
                    }
                    accountMerchants.add(accountMerchant)
                }
            }
        }
        return BaseTransactionResponse.InitTransactionResponse(
            merchantPhone = ltv.getNode(0x34)!!.split("\\")[0].trim(),
            merchantId = receivedIsoMessage.getString("42"),
            merchantName = ltv.getNode(0x31)!!.split("\\")[0].trim().trim(),
            englishMerchantName = ltv.getNode(0x31)!!.split("\\")[1].trim().trim(),
            0,
            null,
            null,
            sendMessage.tranDate,
            request.time,
            receivedIsoMessage.rrn ?: sendMessage.stan,
            dateTimeOfServer = ltv.getNode(0x50),
            accountMerchants = accountMerchants
        )
    }
    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FailedTransactionResponse {
        return if (receivedIsoMessage == null) {
            FailedTransactionResponse(
                responseCode = -1,
                responseMessage = ResponseMessageContainer.RC_1.messageId,
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                stan = sendMessage.stan, posCode = null
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
                stan = sendMessage.stan, posCode = receivedIsoMessage.getField48Tag(0x98) ?: "",
                dateTimeOfServer = dateTimeOfServer
            )
        }
    }

}