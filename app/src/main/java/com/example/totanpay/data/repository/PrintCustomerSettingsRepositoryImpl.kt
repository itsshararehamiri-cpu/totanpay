package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.PrintCustomerSettingsLocalDataSource
import javax.inject.Inject

class PrintCustomerSettingsRepositoryImpl @Inject constructor(
    private val printCustomerSettingsLocalDataSource: PrintCustomerSettingsLocalDataSource
) : PrintCustomerSettingsRepository {
    override fun setPrintStatus(status: PrintStatus, amount: String?) {
        printCustomerSettingsLocalDataSource.setPrinStatus(status)
        if (amount != null)
            printCustomerSettingsLocalDataSource.setMinimumAmountForPrint(amount)
    }

    override fun getPrintStatus(): PrintStatus {
        return printCustomerSettingsLocalDataSource.getPrinStatus()
    }

    override fun getMinimumAmountForPrint(): String {
        return printCustomerSettingsLocalDataSource.getMinimumAmountForPrint() ?: "0"
    }


}