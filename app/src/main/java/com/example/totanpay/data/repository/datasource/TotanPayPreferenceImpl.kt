package com.example.totanpay.data.repository.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.totanpay.data.util.toEnglishNumber
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class TechPayPrefImp @Inject constructor(@ApplicationContext context: Context)
const val STAN = "stan"
const val TERMINAL_ID = "terminal_id"
const val MERCHANT_ID = "merchant_id"
const val MERCHANT_PHONE = "merchant_phone"
const val MERCHANT_NAME = "merchant_name"

const val ENGLISH_MERCHANT_NAME = "english_merchant_name"

const val POS_CODE="pos_code"
const val IP = "ip"
const val PORT = "port"
const val NII = "nii"
const val AQUIRINGINSTITUTIONIDENTIFICATIONCODE = "acquiringinstitutionidentificationcode"

const val MERCHANT_PASS = "merchant_pass"

const val PLAYBACK_STATUS_SOUND = "playback_status_sound"
const val THEME_IS_DARK="theme_is_dark"
const val MAXIMUM_AMOUNT_FOR_PURCHASE="maximum_amount_for_purchase"
const val TAX_FOR_IRANCELL_CHARGE="tax_for_irancell_charge"

const val PRINT_SATUS = "print_status"
const val MIN_AMOUNT_FOR_PRINT_STATUS = "min_amount_for_print_status"
const val PRINT_STATUS_MERCHANT = "print_status_merchant"
const val MIN_AMOUNT_FOR_PRINT_MERCHANT = "min_amount_for_print_merchant"
const val ENABLE_MAC="enable_mac"

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
            .getString(MERCHANT_ID, null) ?: ""
    }

    override fun storeMerchantPhone(merchantPhone: String?) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_PHONE, merchantPhone) }.apply()
    }

    override fun getMerchantPhone(): String {
        return sharedPreferces
            .getString(MERCHANT_PHONE, null) ?: ""
    }

    override fun storeMerchantName(merchantName: String?) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_NAME, merchantName) }.apply()
    }

    override fun getMerchantName(): String {
        return sharedPreferces
            .getString(MERCHANT_NAME, null) ?: ""
    }

    override fun storeEnglishMerchantName(merchantName: String?) {
        sharedPreferces.edit()
            .apply { putString(ENGLISH_MERCHANT_NAME, merchantName) }.apply()
    }

    override fun getEnglishMerchantName(): String {
        return sharedPreferces
            .getString(ENGLISH_MERCHANT_NAME, null) ?: ""
    }
    override fun storeConnectionSettings(ip: String, port: String, nii: String) {
        sharedPreferces.edit()
            .apply { putString(IP, ip) }.apply()
        sharedPreferces.edit()
            .apply { putString(PORT, port) }.apply()
        sharedPreferces.edit()
            .apply { putString(NII, nii) }.apply()
    }

    override fun
            getIP(): String {
        return sharedPreferces
            .getString(IP, "78.157.33.210") ?: ""
    }

    override fun getNii(): String {
        return sharedPreferces
            .getString(NII, "101") ?: ""
    }

    override fun setPrintStatus(status: Int) {
        sharedPreferces.edit()
            .apply { putInt(PRINT_SATUS, status) }.apply()
    }

    override fun getPrintStatus(): Int {
        return sharedPreferces
            .getInt(PRINT_SATUS, 2)
    }

    override fun setAutoPrintCustomerReceipt(f: Boolean) {
        sharedPreferces.edit()
            .apply { putBoolean("auto_print_customer_receipt", f) }.apply()
    }

    override fun getAutoPrintCustomerReceipt(): Boolean {
        return sharedPreferces
            .getBoolean("auto_print_customer_receipt", true) ?: true
    }

    override fun setMinAmountForPrint(minAmount: String) {
        sharedPreferces.edit()
            .apply { putString(MIN_AMOUNT_FOR_PRINT_STATUS, minAmount) }.apply()
    }

    override fun getMinAmountForPrint(): String {
        return sharedPreferces
            .getString(MIN_AMOUNT_FOR_PRINT_STATUS, null) ?: ""
    }

    override fun setPrintStatusMerchant(status: Int) {
        sharedPreferces.edit()
            .apply { putInt(PRINT_STATUS_MERCHANT, status) }.apply()
    }

    override fun getPrintStatusMerchant(): Int {
        return sharedPreferces
            .getInt(PRINT_STATUS_MERCHANT, 1)
    }

    override fun setMinAmountForPrintMerchant(minAmount: String) {
        sharedPreferces.edit()
            .apply { putString(MIN_AMOUNT_FOR_PRINT_MERCHANT, minAmount) }.apply()
    }

    override fun getMinAmountForPrintMerchant(): String {
        return sharedPreferces
            .getString(MIN_AMOUNT_FOR_PRINT_MERCHANT, null) ?: ""
    }

    override fun getPort(): String {
        return sharedPreferces
            .getString(PORT, "2150") ?: ""
    }


    override fun getAcquiringInstitutionIdentificationCode(): String {
        return sharedPreferces
            .getString(AQUIRINGINSTITUTIONIDENTIFICATIONCODE, null) ?: ""
    }

    override fun storeAcquiringInstitutionIdentificationCode(acquiringInstitutionIdentificationCode: String?) {
        sharedPreferces.edit()
            .apply {
                putString(
                    AQUIRINGINSTITUTIONIDENTIFICATIONCODE,
                    acquiringInstitutionIdentificationCode
                )
            }.apply()
    }

    override fun setMerchantPassword(pass: String) {
        sharedPreferces.edit()
            .apply { putString(MERCHANT_PASS, pass) }.apply()
    }

    override fun getMerchantPassword(): String? {
        return sharedPreferces
            .getString(MERCHANT_PASS, null)
    }

    override fun setPlaybackStatusSound(status: Boolean) {
        sharedPreferces.edit()
            .apply { putBoolean(PLAYBACK_STATUS_SOUND,status) }.apply()
    }

    override fun getPlaybackStatusSound(): Boolean {
        return sharedPreferces
            .getBoolean(PLAYBACK_STATUS_SOUND, false)
    }

    override fun setThemeIsDark(status: Boolean) {
        sharedPreferces.edit()
            .apply { putBoolean(THEME_IS_DARK,status) }.apply()
    }

    override fun getThemeIsDark(): Boolean {
        return sharedPreferces
            .getBoolean(THEME_IS_DARK, false)
    }

    override fun setMaximumAmount(amount: String) {
        sharedPreferces.edit()
            .apply { putString(MAXIMUM_AMOUNT_FOR_PURCHASE, amount.toEnglishNumber()) }.apply()
    }

    override fun getMaximumAmount(): String {
        return sharedPreferces
            .getString(MAXIMUM_AMOUNT_FOR_PURCHASE, "1000000000".toEnglishNumber())?:""
    }

    override fun setTaxForIrancellCharge(tax: String) {
        sharedPreferces.edit()
            .apply { putString(TAX_FOR_IRANCELL_CHARGE, tax.toEnglishNumber()) }.apply()
    }

    override fun getTaxForIrancellCharge(): String {
        return sharedPreferces
            .getString(TAX_FOR_IRANCELL_CHARGE, "10".toEnglishNumber())?:""
    }

    override fun setEnableMac(isEnable: Boolean) {
        sharedPreferces.edit()   .apply { putBoolean(ENABLE_MAC, isEnable) }.apply()
    }

    override fun getEnableMac(): Boolean {
        return sharedPreferces
            .getBoolean(ENABLE_MAC, true)
    }

    override fun storePosCode(posCode: String) {
        sharedPreferces.edit()
            .apply { putString(POS_CODE, posCode) }.apply()
    }

    override fun getPosCode(): String {
        return sharedPreferces
            .getString(POS_CODE, "")?:""
    }

    override fun setChangePassword(b: Boolean) {
        sharedPreferces.edit()
            .apply { putBoolean("is_change", b) }.apply()
    }

    override fun isChangePassword(): Boolean {
        return sharedPreferces
            .getBoolean("is_change", false)
    }
}