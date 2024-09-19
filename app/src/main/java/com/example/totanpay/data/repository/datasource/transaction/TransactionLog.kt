package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

data class TransactionLog (
    var id: Long ,
    var processingCode: String ,
    var amount: Long ,
    var stan: String ,
    var dateTransaction: String ,
    var timeTransaction: String ,
    var merchantId: String ,
    var maskedPan:String?,var type:Int,
    var rrn: String?,
    var issuer:String?,
    var responseCode:Int?,
    var responseMsg:String?,var billId:String?=null,var payId:String?=null,var serviceDesc:String?=null,var pinVoucher:String?=null,
    var serialVoucher:String?=null,var mobileNumber:String?=null,var operatorCode:Int?=null
    ,    var terminalId: String ,
    val merchantName:String="",
    val merchantPhone:String?=null,
    val timestamp:Long

    )
fun TransactionLog.toResponseTransaction():ResponseTransaction{
    return  ResponseTransaction(
        responseCode = this.processingCode,
        responseMessage = this.responseMsg ?: "",
        rrn = this.rrn ?: "",
        trace = this.stan,
        merchantName = this.merchantName,
        merchantId = this.merchantId,
        merchantPhone = this.merchantPhone ?: "",
        terminalID = this.terminalId,
        transactionType = this.type.toString(),
        date = this.dateTransaction,
        time = this.timeTransaction,
        issuerName = this.issuer ?: "",
        amount = this.amount.toString(),
        maskedPan = this.maskedPan ?: "",
        voucherPin = this.pinVoucher
    )
}