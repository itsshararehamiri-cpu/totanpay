package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment

enum class PrintStatus(val status: Int) {
    ALWAYS_PRINTING(1),
    NO_PRINTING(2),
    PRINTING_WITH_MIN_AMOUNT(3);

    companion object {
        fun valueOf(status: Int): PrintStatus {
            var temp: PrintStatus? = null
            for (e in PrintStatus.values()) {
                if (e.status == status) {
                    temp = e
                }
            }
            return temp ?: NO_PRINTING
        }
    }
}

interface MerchantSettingsRepository {
    fun setPrinStatus(STATUS: PrintStatus, amount: String? = null)
    fun getPrinStatus(): PrintStatus
    suspend fun setMerchantPassword(pass: String)
    suspend fun merchantPasswordIsValid(pass: String): Boolean
    suspend fun getApportionments(): List<Apportionment>
    suspend fun storeApportionments(apportionments:List<Apportionment>)
    suspend fun needToApportionments():Boolean
}