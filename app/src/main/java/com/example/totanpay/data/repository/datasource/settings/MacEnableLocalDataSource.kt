package com.example.totanpay.data.repository.datasource.settings

interface MacEnableLocalDataSource {
    fun enableMac(isEnable: Boolean)
    fun getEnableMac(): Boolean
}