package com.example.totanpay.data.repository


interface PrintCustomerSettingsRepository {
    fun setPrintStatus(status: PrintStatus, amount: String? = null)
    fun getPrintStatus(): PrintStatus
    fun getMinimumAmountForPrint(): String
}