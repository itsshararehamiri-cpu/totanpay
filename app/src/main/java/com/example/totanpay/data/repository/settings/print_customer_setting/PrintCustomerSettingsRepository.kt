package com.example.totanpay.data.repository.settings.print_customer_setting

import com.example.totanpay.data.repository.settings.merchant.PrintStatus

interface PrintCustomerSettingsRepository {
    fun setPrintStatus(status: PrintStatus, amount: String? = null,autoPrintCustomerReceipt: Boolean)
    fun getPrintStatus(): PrintStatus
    fun getMinimumAmountForPrint(): String


    fun setAutoPrintCustomerReceipt(f: Boolean)
    fun getAutoPrintCustomerReceipt(): Boolean
}