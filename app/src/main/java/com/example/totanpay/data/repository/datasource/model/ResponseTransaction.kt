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
    val transactionType: String,
    val date: String,
    val time: String,
    val issuerName: String,
    val amount: String,
    val availableBalance:String?=null
    ,val maskedPan:String,
    val realBalance:String?=null,val voucherPin:String?=null,
    val voucherSerial:String?=null

)