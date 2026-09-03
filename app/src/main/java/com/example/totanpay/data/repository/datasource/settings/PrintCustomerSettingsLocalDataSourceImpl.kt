package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.datasource.TotanPayPreference
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import javax.inject.Inject

class PrintCustomerSettingsLocalDataSourceImpl @Inject constructor(private val totanPayPreference: TotanPayPreference)
    : PrintCustomerSettingsLocalDataSource {
    override fun setPrinStatus(STATUS: PrintStatus) {
        totanPayPreference.setPrintStatus(STATUS.status)
    }

    override fun setMinimumAmountForPrint(amount: String) {
        totanPayPreference.setMinAmountForPrint(amount)
    }

    override fun getPrinStatus(): PrintStatus {
        return PrintStatus.valueOf(totanPayPreference.getPrintStatus())
    }

    override fun getMinimumAmountForPrint(): String? {
        return totanPayPreference.getMinAmountForPrint()
    }

    override fun setAutoPrintCustomerReceipt(f: Boolean) {
        return totanPayPreference.setAutoPrintCustomerReceipt(f)
    }

    override fun getAutoPrintCustomerReceipt(): Boolean {
        return totanPayPreference.getAutoPrintCustomerReceipt()
    }
}