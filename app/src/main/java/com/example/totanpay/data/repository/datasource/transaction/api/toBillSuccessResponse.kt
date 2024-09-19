package com.example.totanpay.data.repository.datasource.transaction.api

import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.BillPayTransactionResponse.toBillSuccessResponse(
    merchant: Merchant,
    terminalId: String,
    track2: String,
    amount: String,
    billID: String,
    payId: String,
    serviceDesc: String
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
        transactionType = TransactionType.BILL_PAY.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = amount, voucherPin = null,
        maskedPan = extractPanFromTrack2(track2).mask(),
        billId = billID,
        paymentId = payId,
        serviceDesc = serviceDesc
    )
}

fun BaseTransactionResponse.BillPayTransactionResponse.toBillUnSuccessResponse(
    merchant: Merchant,
    terminalId: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = if (this.responseMessage != null) {
            this.responseMessage!!
        } else {
            ResponseMessageContainer.valueOfLabel(
                this.responseCode.toString()
            ).message
        },
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = TransactionType.BILL_PAY.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = "",
        billId = "",
        maskedPan = this.maskedPan,
    )
}

fun BaseTransactionResponse.BillInquiryTransactionResponse.toBillInquirySuccessResponse(
    merchant: Merchant, terminalId: String,
    billID: String, payId: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = this.responseMessage ?: "",
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = TransactionType.BILL_INQUERY.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = "",
        amount = this.amount ?: "",
        voucherPin = null,
        maskedPan = "",
        billId = billID,
        paymentId = payId,
        serviceDesc = this.serviceDesc,
        billType = this.billType
    )
}

fun BaseTransactionResponse.BillInquiryTransactionResponse.toBillInquiryUnSuccessResponse(
    merchant: Merchant,
    terminalId: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = this.responseMessage.toString(),
        rrn = this.rrn ?: "",
        trace = this.trace ?: "",
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = TransactionType.BILL_INQUERY.title,
        date = this.date ?: "",
        time = this.time?.formatTime() ?: "",
        issuerName = "",
        amount = "",
        billId = "",
        maskedPan = "",
    )
}