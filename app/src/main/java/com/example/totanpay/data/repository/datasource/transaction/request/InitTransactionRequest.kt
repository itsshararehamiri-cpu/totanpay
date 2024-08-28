package com.example.totanpay.data.repository.datasource.transaction.request


class InitTransactionRequest(
    val terminalId: String,
    stan: Int, serial: String, appVersion: String, nii: String,date:String,time:String
) :
    BaseTransactionRequest(serial=serial, appVersion=appVersion, nii=nii, stan=stan,
        date=date,time=time)