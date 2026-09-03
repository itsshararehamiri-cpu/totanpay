package com.example.totanpay.data.repository.datasource.transaction.request

class LogonTransactionRequest (stan: Int, serial: String, appVersion: String, nii: String,date:String,time:String,terminalLanguage:String
, terminalConnectionType:String, posConditionCode:String,val enableMac: Boolean):
     BaseTransactionRequest( serial=serial, appVersion=appVersion,terminalLanguage=terminalLanguage, nii=nii,
          stan=stan,date=date,time=time,posConditionCode=posConditionCode, terminalConnectionType = terminalConnectionType)