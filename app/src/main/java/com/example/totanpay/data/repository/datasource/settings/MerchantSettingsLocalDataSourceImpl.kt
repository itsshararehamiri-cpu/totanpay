package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.datasource.TotanPayPreference
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import javax.inject.Inject

class MerchantSettingsLocalDataSourceImpl @Inject constructor(private val totanPayPreference: TotanPayPreference)
    : MerchantSettingsLocalDataSource {
    override fun setPrinStatus(status: PrintStatus) {
        totanPayPreference.setPrintStatusMerchant(status.status)
    }

    override fun setMinimumAmountForPrint(amount: String) {
        totanPayPreference.setMinAmountForPrintMerchant(amount)
    }

    override fun getPrinStatus(): PrintStatus {
        return PrintStatus.valueOf(totanPayPreference.getPrintStatusMerchant())
    }

    override fun getMinimumAmountForPrint(): String? {
        return totanPayPreference.getMinAmountForPrintMerchant().takeIf { it.isNotEmpty() }
    }

    override fun setMerchantPassword(pass: String) {
        totanPayPreference.setMerchantPassword(pass)
    }

    override fun getMerchantPassword(): String? {
      return totanPayPreference.getMerchantPassword()
    }

    override fun isChangePassword(): Boolean {
        return totanPayPreference.isChangePassword()
    }

    override fun setChangePassword(b: Boolean) {
        totanPayPreference.setChangePassword(b)
    }
}