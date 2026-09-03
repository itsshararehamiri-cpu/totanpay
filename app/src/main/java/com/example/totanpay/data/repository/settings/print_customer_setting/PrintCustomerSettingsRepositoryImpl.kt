package com.example.totanpay.data.repository.settings.print_customer_setting

import com.example.totanpay.data.repository.datasource.settings.PrintCustomerSettingsLocalDataSource
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import javax.inject.Inject

class PrintCustomerSettingsRepositoryImpl @Inject constructor(
    private val printCustomerSettingsLocalDataSource: PrintCustomerSettingsLocalDataSource
) : PrintCustomerSettingsRepository {
    override fun setPrintStatus(status: PrintStatus, amount: String?,autoPrintCustomerReceipt: Boolean) {
        printCustomerSettingsLocalDataSource.setPrinStatus(status)
        printCustomerSettingsLocalDataSource.setAutoPrintCustomerReceipt(autoPrintCustomerReceipt)
        if (amount != null)
            printCustomerSettingsLocalDataSource.setMinimumAmountForPrint(amount)
        else     printCustomerSettingsLocalDataSource.setMinimumAmountForPrint("")
    }

    override fun getPrintStatus(): PrintStatus {
        return printCustomerSettingsLocalDataSource.getPrinStatus()
    }

    override fun getMinimumAmountForPrint(): String {
        return printCustomerSettingsLocalDataSource.getMinimumAmountForPrint() ?: ""
    }

    override fun setAutoPrintCustomerReceipt(f: Boolean) {
        printCustomerSettingsLocalDataSource.setAutoPrintCustomerReceipt(f)
    }

    override fun getAutoPrintCustomerReceipt(): Boolean {
       return printCustomerSettingsLocalDataSource.getAutoPrintCustomerReceipt()
    }


}