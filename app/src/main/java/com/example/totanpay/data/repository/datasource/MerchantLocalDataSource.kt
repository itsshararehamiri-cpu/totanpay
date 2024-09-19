package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.response.AccountMerchant

interface MerchantLocalDataSource {
    fun storeTerminalId(terminalId: String)
    fun storeMerchant(merchantId: String?, merchantPhone: String?, merchantName: String?)
    suspend fun storeAllAccountMerchants(accountMerchants: List<AccountMerchant>)
    suspend fun getAllAccountMerchants():List<AccountMerchant>?
    fun getMerchant(): Merchant
    fun getTerminalId(): String
    fun storeAcquiringInstitutionIdentificationCode(acquiringInstitutionIdentificationCode: String)
    fun getAcquiringInstitutionIdentificationCode(): String?
    fun getApportionments():List<Apportionment>?
    fun storeApportionments(apportionments:List<Apportionment>)
    fun getTerminalLanguage(): String
    fun getCurrency():String
    suspend fun terminalHasApportionments():Boolean
    suspend fun isSetpportionments():Boolean

}