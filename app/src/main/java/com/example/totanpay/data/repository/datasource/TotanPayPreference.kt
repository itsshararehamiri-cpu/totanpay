package com.example.totanpay.data.repository.datasource

interface TotanPayPreference {
    fun storeStan(stan: Int)
    fun getStan(): Int
    fun storeTerminalId(terminalId: String)
    fun getTerminalId(): String?


    fun storeAcquiringInstitutionIdentificationCode(acquiringInstitutionIdentificationCode: String?)
    fun getAcquiringInstitutionIdentificationCode(): String


    fun storeMerchantPhone(merchantPhone: String?)
    fun getMerchantPhone(): String


    fun storeMerchantName(merchantName: String?)
    fun getMerchantName(): String


    fun storeMerchantId(merchantId: String?)
    fun getMerchantId(): String


    fun storeConnectionSettings(ip: String, port: String, nii: String)
    fun getIP(): String
    fun getPort(): String
    fun getNii(): String


    fun setPrintStatus(status: Int)
    fun getPrintStatus(): Int


    fun setMinAmountForPrint(minAmount: String)
    fun getMinAmountForPrint(): String
    fun setMerchantPassword(pass: String)
    fun getMerchantPassword(): String?

    fun setPlaybackStatusSound(status: Boolean)
    fun getPlaybackStatusSound(): Boolean


    fun setThemeIsDark(status: Boolean)
    fun getThemeIsDark(): Boolean



    fun setMaximumAmount(minAmount: String)
    fun getMaximumAmount(): String


    fun setTaxForIrancellCharge(tax: String)
    fun getTaxForIrancellCharge(): String


}