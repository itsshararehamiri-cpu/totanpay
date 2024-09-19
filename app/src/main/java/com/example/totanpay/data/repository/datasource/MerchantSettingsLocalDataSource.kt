package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.PrintStatus

interface MerchantSettingsLocalDataSource {
    fun setPrinStatus(STATUS: PrintStatus)
    fun setMinimumAmountForPrint(amount:String)
    fun getPrinStatus(): PrintStatus
    fun setMerchantPassword(pass:String)
    fun getMerchantPassword():String?
}