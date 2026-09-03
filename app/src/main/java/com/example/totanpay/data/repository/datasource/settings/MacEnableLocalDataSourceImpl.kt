package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.datasource.TotanPayPreference
import javax.inject.Inject

class MacEnableLocalDataSourceImpl @Inject constructor(private val preference: TotanPayPreference):MacEnableLocalDataSource {
    override fun enableMac(isEnable: Boolean) {
       preference.setEnableMac(isEnable)
    }

    override fun getEnableMac(): Boolean {
      return preference.getEnableMac()
    }
}