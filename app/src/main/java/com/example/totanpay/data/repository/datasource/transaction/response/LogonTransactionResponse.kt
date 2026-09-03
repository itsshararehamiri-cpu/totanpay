package com.example.totanpay.data.repository.datasource.transaction.response

import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.WorkingKeyExchangeTransactionResponse.toResponseTransaction(): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = this.responseMessage ?: R.string.empty_message,
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = "",
        merchantId = "",
        merchantPhone = "",
        terminalID = "",
        transactionType = TransactionType.LOGON.title,
        date = this.date,
        time = this.time,
        issuerName = "",
        amount = "",
        availableBalance = null, maskedPan = "",
        realBalance = null, voucherPin = null, englishMerchantName = ""
    )
}

fun BaseTransactionResponse.LogonTransactionResponse.toResponseTransaction(): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage =  if(this.responseMessage!=null){
            this.responseMessage!!
        }else {
            ResponseMessageContainer.valueOfLabel(
                this.responseCode.toString()
            ).messageId
        },
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = "",
        merchantId = "",
        merchantPhone = "",
        terminalID = this.terminalId,
        transactionType = TransactionType.LOGON.title,
        date = this.date,
        time = this.time,
        issuerName = "",
        amount = "",
        availableBalance = null, maskedPan = "",
        realBalance = null, voucherPin = null, englishMerchantName = ""
    )
}
