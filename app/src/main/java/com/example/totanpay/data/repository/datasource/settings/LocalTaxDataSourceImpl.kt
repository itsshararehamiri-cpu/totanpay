package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.datasource.TotanPayPreference
import javax.inject.Inject

class LocalTaxDataSourceImpl @Inject constructor(private val totanPayPreference: TotanPayPreference) :
    LocalTaxDataSource {
    override fun setTax(tax: String) {
        totanPayPreference.setTaxForIrancellCharge(tax)
    }

    override fun getTax(): String {
        return totanPayPreference.getTaxForIrancellCharge()
    }
}