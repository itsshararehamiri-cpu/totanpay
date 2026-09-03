package com.example.totanpay.data.repository.datasource.transaction.request


class GetKeyTransactionRequest(
    val compressedPOSPublicKey: String,
    val hashedOtp: String,
    val dateTimeInGMT: String,
    stan: Int, serial: String, appVersion: String,terminalLanguage:String, nii: String, date:String, time:String,
    posConditionCode:String
) :
    BaseTransactionRequest(serial=serial, appVersion=appVersion,terminalLanguage=terminalLanguage,
        nii=nii, stan=stan,
        date=date,time=time, posConditionCode = posConditionCode, terminalConnectionType = "")