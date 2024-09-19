package com.example.totanpay.data.repository.datasource.transaction.api

import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.VoucherTransactionResponse.toVoucherSuccessResponse(
    merchant: Merchant, terminalId: String, track2: String, operator: Operator
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
        transactionType = TransactionType.VOUCHER.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = amount,
        voucherPin = this.voucherPin,
        maskedPan = extractPanFromTrack2(track2).mask(),
        voucherSerial = this.voucherSerial,
        productCode = operator.code
    )
}

fun BaseTransactionResponse.VoucherTransactionResponse.toVoucherUnSuccessResponse(
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
        transactionType = TransactionType.VOUCHER.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = this.amount,
        availableBalance = "",
        maskedPan = this.maskedPan,
        realBalance = null,
        voucherPin = this.voucherPin,
        voucherSerial = this.voucherSerial
    )
}