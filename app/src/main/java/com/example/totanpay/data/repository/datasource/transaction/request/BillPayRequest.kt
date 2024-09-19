package com.example.totanpay.data.repository.datasource.transaction.request

class BillPayTransactionRequest (val amount:String,val pan:String,stan: Int, val terminalId:String,
                                  terminalLanguage:String,
                                  terminalConnectionType:String,
                                 val terminalType:String,
                                 val track2:String,
                                 val pinBlock:String,
                                 val merchantId:String,
                                 val currency:String,
                                 val POS:String,
                                 val billId:String,
                      val payId:String,
                      val serviceDesc:String,
                                 serial: String, appVersion: String, nii: String,date:String,time:String,
                                 posConditionCode:String):
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time, terminalLanguage = terminalLanguage, posConditionCode = posConditionCode, terminalConnectionType = terminalConnectionType
    )