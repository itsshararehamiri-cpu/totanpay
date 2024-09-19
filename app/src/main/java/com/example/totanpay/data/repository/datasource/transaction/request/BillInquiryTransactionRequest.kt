package com.example.totanpay.data.repository.datasource.transaction.request



class BillInquiryTransactionRequest (
    stan: Int,
                                     val merchantId:String,
                                     val terminalId:String,
                                      terminalLanguage:String,
                                      terminalConnectionType:String,
                                     val terminalType:String,
                                     val billId:String,val payId:String,
                                     serial: String, appVersion: String, nii: String, date:String, time:String,
    posConditionCode:String):
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time, terminalLanguage = terminalLanguage, posConditionCode = posConditionCode, terminalConnectionType = terminalConnectionType
    )