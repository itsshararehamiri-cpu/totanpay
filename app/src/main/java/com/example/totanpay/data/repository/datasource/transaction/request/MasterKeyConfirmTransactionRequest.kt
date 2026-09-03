package com.example.totanpay.data.repository.datasource.transaction.request

class MasterKeyConfirmTransactionRequest (stan: Int, serial: String, appVersion: String, nii: String, date:String, time:String
,val hashedMasterToken:String,val terminalId:String,terminalLanguage:String,
     val acquiringInstitutionIdentificationCode:String,posConditionCode:String):
     BaseTransactionRequest( serial=serial, appVersion=appVersion,terminalLanguage=terminalLanguage,
          nii=nii,stan=stan,date=date,
          time=time, posConditionCode = posConditionCode, terminalConnectionType = "")