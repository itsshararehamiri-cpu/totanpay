package com.example.totanpay.data.repository.datasource.transaction.request


class WorkingKeyExchangeTransactionRequest(
    val terminalId:String,
    val masterToken: String,
    val dateTimeInGMT: String,
    stan: Int, serial: String, appVersion: String, nii: String,date:String,time:String,terminalLanguage:String,
    posConditionCode:String
) :
    BaseTransactionRequest(serial=serial, appVersion=appVersion,terminalLanguage=terminalLanguage, nii=nii, stan=stan,
        date=date,time=time,posConditionCode=posConditionCode,terminalConnectionType="")// TODO:  