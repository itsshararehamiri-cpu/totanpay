package com.example.totanpay.data.repository.datasource.settings

import com.example.totanpay.data.repository.settings.merchant.PrintStatus

interface MerchantSettingsLocalDataSource {
    fun setPrinStatus(STATUS: PrintStatus)
    fun setMinimumAmountForPrint(amount:String)
    fun getPrinStatus(): PrintStatus
    fun getMinimumAmountForPrint(): String?
    fun setMerchantPassword(pass:String)
    fun getMerchantPassword():String?
    fun isChangePassword(): Boolean
    fun setChangePassword(b: Boolean)

}