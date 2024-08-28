package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.datasource.model.Merchant

interface TotanPayPreference {
    fun storeStan(stan: Int)
    fun getStan(): Int
    fun storeTerminalId(terminalId: String)
    fun getTerminalId(): String?


    fun storeMerchantId(merchantId: String?)
    fun getMerchantId(): String


    fun storeMerchantPhone(merchantPhone: String?)
    fun getMerchantPhone(): String


    fun storeMerchantName(merchantName: String?)
    fun getMerchantName(): String



    fun storeConnectionSettings(ip: String,port:String,nii:String)
    fun getIP(): String
    fun getPort(): String
    fun getNii(): String

}