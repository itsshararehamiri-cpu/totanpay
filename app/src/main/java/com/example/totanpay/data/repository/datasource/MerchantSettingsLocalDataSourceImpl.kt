package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.PrintStatus
import javax.inject.Inject

class MerchantSettingsLocalDataSourceImpl @Inject constructor(private val totanPayPreference: TotanPayPreference)
    : MerchantSettingsLocalDataSource {
    override fun setPrinStatus(status: PrintStatus) {
        totanPayPreference.setPrintStatus(status.status)
    }

    override fun setMinimumAmountForPrint(amount: String) {
        totanPayPreference.setMinAmountForPrint(amount)
    }

    override fun getPrinStatus(): PrintStatus {
        return PrintStatus.valueOf(totanPayPreference.getPrintStatus())
    }

    override fun setMerchantPassword(pass: String) {
        totanPayPreference.setMerchantPassword(pass)
    }

    override fun getMerchantPassword(): String? {
      return totanPayPreference.getMerchantPassword()
    }
}