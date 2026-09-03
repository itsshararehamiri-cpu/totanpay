package com.example.totanpay.data.repository.datasource.transaction.api

import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.VoucherTransactionResponse.toVoucherSuccessResponse(isFarsi: Boolean,
    merchant: Merchant, terminalId: String, track2: String, operator: Operator, posCode: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this!!.responseCode.toString(),
        responseMessage = this.responseMessage ?: R.string.empty_message,
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName =if(isFarsi) merchant.merchantName ?: ""
        else merchant.englishMerchantName?:"",
        merchantId = merchant.merchantId ?: "",
        merchantPhone =if(isFarsi) merchant.merchantPhone ?: "" else merchant.englishMerchantName?:"",
        terminalID = terminalId,
        transactionType = TransactionType.VOUCHER.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = amount,
        voucherPin = this.voucherPin,
        maskedPan = extractPanFromTrack2(track2).mask(),
        voucherSerial = this.voucherSerial,
        productCode = operator.code, posCode = posCode, englishMerchantName = merchant.englishMerchantName?:""
    )
}

fun BaseTransactionResponse.VoucherTransactionResponse.toVoucherUnSuccessResponse(
    merchant: Merchant,
    terminalId: String,posCode: String
): ResponseTransaction {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = if (this.responseMessage != null) {
            this.responseMessage!!
        } else {
            ResponseMessageContainer.valueOfLabel(
                this.responseCode.toString()
            ).messageId
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
        voucherSerial = this.voucherSerial,posCode=posCode, englishMerchantName = merchant.englishMerchantName?:""
    )
}