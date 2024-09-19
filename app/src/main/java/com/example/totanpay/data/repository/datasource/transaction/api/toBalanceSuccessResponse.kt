package com.example.totanpay.data.repository.datasource.transaction.api

import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.formatTime

fun BaseTransactionResponse.BalanceTransactionResponse.toBalanceSuccessResponse(
    track2:String,merchant:Merchant,terminalId:String
): ResponseTransaction {
   return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = this.responseMessage ?: "",
        rrn =this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = TransactionType.BALANCE.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = "",
        availableBalance = this.availableBalance,
        realBalance = this.balance,
        maskedPan = extractPanFromTrack2(track2).mask(), dateTimeOfServer = this.dateTimeOfServer
    )
}
fun BaseTransactionResponse.BalanceTransactionResponse.toBalanceUnSuccessResponse(
     track2:String,merchant:Merchant,terminalId:String
): ResponseTransaction {
     return ResponseTransaction(
          responseCode = this.responseCode.toString(),
          responseMessage = this.responseMessage ?: "",
          rrn =this.rrn ?: "",
          trace = this.trace,
          merchantName = merchant.merchantName ?: "",
          merchantId = merchant.merchantId ?: "",
          merchantPhone = merchant.merchantPhone ?: "",
          terminalID = terminalId,
          transactionType = TransactionType.BALANCE.title,
          date = this.date,
          time = this.time.formatTime(),
          issuerName = this.issuerName,
          amount = "",
          availableBalance = this.availableBalance,
          realBalance = this.balance,
          maskedPan = extractPanFromTrack2(track2).mask()
     )
}