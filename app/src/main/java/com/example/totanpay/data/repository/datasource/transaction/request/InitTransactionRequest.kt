package com.example.totanpay.data.repository.datasource.transaction.request


class InitTransactionRequest(
    val terminalId: String,
    val dateTimeInGMT: String,
    stan: Int,
    serial: String,
    appVersion: String,
    nii: String,
    date: String,
    time: String,
    terminalLanguage: String,
    val merchantId: String,
    val acquiringInstitutionIdentificationCode: String,
    posConditionCode: String,
    terminalConnectionType: String
) :
    BaseTransactionRequest(
        serial = serial,
        appVersion = appVersion,
        terminalLanguage = terminalLanguage,
        nii = nii,
        stan = stan,
        date = date,
        time = time,
        posConditionCode = posConditionCode,
        terminalConnectionType = terminalConnectionType
    )