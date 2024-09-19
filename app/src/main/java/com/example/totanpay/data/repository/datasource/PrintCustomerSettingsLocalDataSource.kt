package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.PrintStatus

interface PrintCustomerSettingsLocalDataSource {
    fun setPrinStatus(STATUS: PrintStatus)
    fun setMinimumAmountForPrint(amount:String)
    fun getPrinStatus(): PrintStatus
    fun getMinimumAmountForPrint():String?

}