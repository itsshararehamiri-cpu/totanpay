package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.MerchantLocalDataSource
import com.example.totanpay.data.util.HMACSHA512
import com.example.totanpay.data.repository.datasource.MerchantSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MerchantSettingsRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val merchantSettingsLocalDataSource: MerchantSettingsLocalDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource
) : MerchantSettingsRepository {
    override fun setPrinStatus(STATUS: PrintStatus, amount: String?) {
        merchantSettingsLocalDataSource.setPrinStatus(STATUS)
        if (amount != null)
            merchantSettingsLocalDataSource.setMinimumAmountForPrint(amount)
    }

    override fun getPrinStatus(): PrintStatus {
        return merchantSettingsLocalDataSource.getPrinStatus()
    }

    override suspend fun setMerchantPassword(pass: String) {
        withContext(ioDispatcher) {
            merchantSettingsLocalDataSource.setMerchantPassword(HMACSHA512(pass))
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
                                amount = ""
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
}