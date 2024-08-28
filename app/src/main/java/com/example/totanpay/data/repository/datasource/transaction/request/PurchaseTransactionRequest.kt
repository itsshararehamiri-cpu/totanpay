package com.example.totanpay.data.repository.datasource.transaction.request


class PurchaseTransactionRequest (val amount:String,val pan:String,stan: Int, val terminalId:String,
                                 val terminalLanguage:String,
                                 val terminalConnectionType:String,
                                 val terminalType:String,
                                 val track2:String,
                                 val pinBlock:String,
                                 val merchantId:String,
                                 val currency:String,
                                 val POS:String,
                                 serial: String, appVersion: String, nii: String,date:String,time:String):
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time
    )