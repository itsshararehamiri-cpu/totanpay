package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.PrintStatus
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
}