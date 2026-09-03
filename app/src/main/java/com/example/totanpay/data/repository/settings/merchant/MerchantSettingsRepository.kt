package com.example.totanpay.data.repository.settings.merchant

import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment

enum class PrintStatus(val status: Int) {
    NO_PRINTING(1),
    PRINT(2);

    companion object {
        fun valueOf(status: Int): PrintStatus {
            var temp: PrintStatus? = null
            for (e in values()) {
                if (e.status == status) {
                    temp = e
                }
            }
            return temp ?: NO_PRINTING
        }
    }
}

interface MerchantSettingsRepository {
    fun setPrintStatus(STATUS: PrintStatus)
    fun getPrinStatus(): PrintStatus
    fun getMinimumAmountForPrint(): String
    suspend fun setMerchantPassword(pass: String)
    suspend fun merchantPasswordIsValid(pass: String): Boolean
    suspend fun getApportionments(): List<Apportionment>
    suspend fun storeApportionments(apportionments:List<Apportionment>)
    suspend fun needToApportionments():Boolean


    fun isChangePassword(): Boolean
}