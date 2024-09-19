package com.example.totanpay.data.repository.datasource.transaction.request


class BalanceTransactionRequest(
    val pan: String,
    val terminalId: String,
     terminalLanguage: String,
     terminalConnectionType: String,
    val terminalType: String,
    val track2: String,
    val pinBlock: String,
    val merchantId: String,
    val currency: String,
    val POS: String,
    stan: Int,
    serial: String, appVersion: String, nii: String, date: String, time: String, posConditionCode:String
) :
    BaseTransactionRequest(
        serial = serial, appVersion = appVersion, nii = nii, stan = stan,
        date = date, time = time, terminalLanguage = terminalLanguage, posConditionCode = posConditionCode, terminalConnectionType = terminalConnectionType
    )