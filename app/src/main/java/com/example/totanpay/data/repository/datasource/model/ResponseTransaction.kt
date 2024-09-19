package com.example.totanpay.data.repository.datasource.model

data class ResponseTransaction(
    val responseCode: String,
    val responseMessage: String,
    val rrn: String,
    val trace: String,
    val merchantName: String,
    val merchantId: String,
    val merchantPhone:String,
    val terminalID: String,
    var  transactionType: String,
    var date: String,
    var time: String,
    val issuerName: String,
    val amount: String,
    val availableBalance:String?=null
    ,val maskedPan:String,
    val realBalance:String?=null,val voucherPin:String?=null,
    val voucherSerial:String?=null,
    val serviceDesc:String?=null,
    val billType:String?=null,
    val billId:String?=null,
    val paymentId:String?=null,
    val productCode:Int?=null,
    val mobile:String?=null,
    val posCode:String?=null,
    val purchaseId:String?=null,val dateTimeOfServer: String?=null)