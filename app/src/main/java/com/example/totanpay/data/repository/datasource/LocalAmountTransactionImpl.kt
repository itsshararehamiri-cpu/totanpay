package com.example.totanpay.data.repository.datasource

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