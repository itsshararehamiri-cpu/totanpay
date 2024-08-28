package com.example.totanpay.data.repository.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.totanpay.data.repository.datasource.model.Merchant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class TechPayPrefImp @Inject constructor(@ApplicationContext context: Context)
const val STAN = "stan"
const val TERMINAL_ID="terminal_id"
const val MERCHANT_ID="merchant_id"
const val MERCHANT_PHONE="merchant_phone"
const val MERCHANT_NAME="merchant_name"
const val IP="ip"
const val PORT="port"
const val NII="nii"
@Singleton
class TotanPayPreferenceImpl @Inject constructor(@ApplicationContext context: Context) :
    TotanPayPreference {
    private val sharedPreferces: SharedPreferences

    init {
        sharedPreferces = context.getSharedPreferences("totan_pay_pref", Context.MODE_PRIVATE)
    }

    override fun storeStan(stan: Int) {
        sharedPreferces.edit()
            .apply { putInt(STAN, stan) }.apply()
    }

    override fun getStan(): Int {
        return sharedPreferces
            .getInt(STAN, 0)
    }

    override fun storeTerminalId(terminalId: String) {
        sharedPreferces.edit()
            .apply { putString(TERMINAL_ID, terminalId) }.apply()
    }

    override fun getTerminalId(): String? {
        return sharedPreferces
            .getString(TERMINAL_ID, null)
    }

    override fun storeMerchantId(merchantId: String?) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_ID, merchantId) }.apply()
    }

    override fun getMerchantId(): String {
        return sharedPreferces
            .getString(MERCHANT_ID, null)?:""
    }

    override fun storeMerchantPhone(merchantPhone: String?) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_PHONE, merchantPhone) }.apply()
    }

    override fun getMerchantPhone(): String {
        return sharedPreferces
            .getString(MERCHANT_PHONE, null)?:""
    }

    override fun storeMerchantName(merchantName: String?) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_NAME, merchantName) }.apply()
    }

    override fun getMerchantName(): String {
        return sharedPreferces
            .getString(MERCHANT_NAME, null)?:""
    }

    override fun storeConnectionSettings(ip: String, port: String, nii: String) {
        sharedPreferces.edit()
            .apply { putString(IP, ip) }.apply()
        sharedPreferces.edit()
            .apply { putString(PORT, port) }.apply()
        sharedPreferces.edit()
            .apply { putString(NII, nii) }.apply()
    }

    override fun getIP(): String {
        return sharedPreferces
            .getString(IP, null)?:""
    }

    override fun getNii(): String {
        return sharedPreferces
            .getString(NII, null)?:""    }

    override fun getPort(): String {
        return sharedPreferces
            .getString(PORT, null)?:""
    }


}