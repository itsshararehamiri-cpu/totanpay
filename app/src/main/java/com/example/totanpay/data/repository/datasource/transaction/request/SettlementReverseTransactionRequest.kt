package com.example.totanpay.data.repository.datasource.transaction.request


class SettlementReverseTransactionRequest (val status:Char,
                                           val processingCode:String,
                                           val amount:String,stan: Int, val terminalId:String,
                                   terminalConnectionType:String,
                                  val terminalType:String,
                                  val merchantId:String,
                                  val currency:String,terminalLanguage:String,
                                  serial: String, appVersion: String, nii: String,date:String,time:String,
                                           posConditionCode:String):
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time, terminalLanguage = terminalLanguage, posConditionCode = posConditionCode,terminalConnectionType=terminalConnectionType
    )