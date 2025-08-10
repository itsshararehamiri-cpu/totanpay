package com.example.totanpay.data.repository.datasource.transaction.api


import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.PurchaseTransactionResponse.toPurchaseSuccessResponse(
    merchant: Merchant,
    terminalId: String,
    track2: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this!!.responseCode.toString(),
        responseMessage = this.responseMessage ?: "",
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = if (purchaseId.isNullOrEmpty()) TransactionType.PURCHASE.title else TransactionType.PURCHASEWITHID.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = amount,
        maskedPan = extractPanFromTrack2(track2).mask(),
        posCode = this!!.posCode,
        purchaseId = this!!.purchaseId, dateTimeOfServer = this.dateTimeOfServer
    )
}

fun BaseTransactionResponse.PurchaseTransactionResponse.toPurchaseUnSuccessResponse(
    purchaseId: String?,
    merchant: Merchant,
    terminalId: String
): ResponseTransaction? {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = if (this.responseMessage.isNullOrEmpty()) {
            ResponseMessageContainer.valueOfLabel(
                this.responseCode.toString()
            ).message
        } else {
            this.responseMessage ?: ""
        },
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = if (purchaseId.isNullOrEmpty()) TransactionType.PURCHASE.title else TransactionType.PURCHASEWITHID.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = this.amount, maskedPan = this.maskedPan, posCode = this.posCode
    )
}