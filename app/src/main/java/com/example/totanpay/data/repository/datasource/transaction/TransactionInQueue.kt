package com.example.totanpay.data.repository.datasource.transaction


data class TransactionInQueue (
     var dateTime:String, var date:String, var time:String, var status:Char, var printed:Boolean
    ,
    var processingCode: String,
    var amount: String,
    var stan: String,
    var merchantId: String,
    var maskedPan:String?,
    var type:Int,
    var rrn: String?,
    var issuer:String?,
    var responseCode:Int?,
    var responseMsg:String?, )
