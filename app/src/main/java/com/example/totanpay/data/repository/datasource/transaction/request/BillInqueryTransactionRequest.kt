package com.example.totanpay.data.repository.datasource.transaction.request



class BillInqueryTransactionRequest (stan: Int,
                                     val merchantId:String,
                                     val terminalId:String,
                                     val terminalLanguage:String,
                                     val terminalConnectionType:String,
                                     val terminalType:String,
                                     val billId:String,val payId:String,
                                     serial: String, appVersion: String, nii: String, date:String, time:String):
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time
    )