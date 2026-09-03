package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.datasource.TotanPayPreference
import javax.inject.Inject

class LocalAmountTransactionImpl @Inject constructor(private val totanPayPreference: TotanPayPreference) :
    LocalAmountTransaction {
    override fun setMaximumAmount(amount: String) {
        totanPayPreference.setMaximumAmount(amount)
    }

    override fun getMaximumAmount(): String {
        return totanPayPreference.getMaximumAmount()
    }
}