package com.example.totanpay.data.repository.settings.merchant

import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.util.HMACSHA512
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantSettingsRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val merchantSettingsLocalDataSource: MerchantSettingsLocalDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val device: IDevice
) : MerchantSettingsRepository {
    override fun setPrintStatus(STATUS: PrintStatus) {
        merchantSettingsLocalDataSource.setPrinStatus(STATUS)

    }

    override fun getPrinStatus(): PrintStatus {
        return merchantSettingsLocalDataSource.getPrinStatus()
    }

    override fun getMinimumAmountForPrint(): String {
        return merchantSettingsLocalDataSource.getMinimumAmountForPrint() ?: ""
    }

    override suspend fun setMerchantPassword(pass: String) {
        withContext(ioDispatcher) {
            merchantSettingsLocalDataSource.setMerchantPassword(HMACSHA512(pass))
            merchantSettingsLocalDataSource.setChangePassword(true)
        }
    }


    override suspend fun merchantPasswordIsValid(enteredPass: String): Boolean {
        return withContext(ioDispatcher) {
            val storedPassword =
                if (merchantSettingsLocalDataSource.getMerchantPassword() != null) {
                    merchantSettingsLocalDataSource.getMerchantPassword()!!
                } else {
                    HMACSHA512("1111")
                }

            HMACSHA512(enteredPass) == storedPassword
        }
    }

    override suspend fun getApportionments(): List<Apportionment> {
        return withContext(ioDispatcher) {
            val apportionments = merchantLocalDataSource.getApportionments()
            if (!apportionments.isNullOrEmpty()) {
                apportionments
            } else {
                val allAccountMerchants = merchantLocalDataSource.getAllAccountMerchants()
                if (allAccountMerchants.isNullOrEmpty()) {
                    listOf()
                } else {
                    val temp: MutableList<Apportionment> = mutableListOf()
                    allAccountMerchants.forEach {
                        temp.add(
                            Apportionment(
                                IBAN = it.number ?: "",
                                bankName = it.farsiBankName ?: "",
                                amount = "",
                                englishBankName=it.bankName?:""
                            )
                        )
                    }
                    temp
                }
            }

        }
    }

    override suspend fun storeApportionments(apportionments: List<Apportionment>) {
        withContext(ioDispatcher) {
            merchantLocalDataSource.storeApportionments(apportionments)
        }
    }

    override suspend fun needToApportionments(): Boolean {
        return withContext(ioDispatcher) {
            val allAccountMerchants = merchantLocalDataSource.getAllAccountMerchants()
            if (allAccountMerchants == null) {
                false
            } else if (allAccountMerchants.size == 1) {
                false
            } else {
                val apportionments = merchantLocalDataSource.getApportionments()
                if (apportionments == null) {
                    true
                } else {
                    if (apportionments.isEmpty())
                        true
                    else false
                }
            }
        }
    }

    override fun isChangePassword(): Boolean {
        return merchantSettingsLocalDataSource.isChangePassword()
    }
}