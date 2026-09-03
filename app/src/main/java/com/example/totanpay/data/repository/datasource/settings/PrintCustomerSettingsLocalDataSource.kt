package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.settings.merchant.PrintStatus

interface PrintCustomerSettingsLocalDataSource {
    fun setPrinStatus(STATUS: PrintStatus)
    fun setMinimumAmountForPrint(amount:String)
    fun getPrinStatus(): PrintStatus
    fun getMinimumAmountForPrint():String?
    fun setAutoPrintCustomerReceipt(f: Boolean)
    fun getAutoPrintCustomerReceipt(): Boolean
}