package com.example.totanpay.data.repository.datasource.transaction.request

sealed class BaseTransactionRequest(
    val serial: String,
    val appVersion: String,
    val terminalLanguage: String,
    val nii: String,
    val stan: Int,
    val date: String,
    val time: String,
    var posConditionCode: String,
    val terminalConnectionType: String
)
