package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.dao.AccountMerchantDao
import com.example.totanpay.data.dao.ApportionmentDao
import com.example.totanpay.data.entity.AccountMerchantEntity
import com.example.totanpay.data.entity.ApportionmentEntity
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.response.AccountMerchant
import javax.inject.Inject


class MerchantLocalDataSourceImpl @Inject constructor(
    private val totanPayPreference: TotanPayPreference,
    private val accountMerchantDao: AccountMerchantDao,
    private val apportionmentDao: ApportionmentDao
) : MerchantLocalDataSource {
    override fun storeTerminalId(terminalId: String) {
        totanPayPreference.storeTerminalId(terminalId)
    }

    override fun storeMerchant(merchantId: String?, merchantPhone: String?, merchantName: String?) {
        totanPayPreference.storeMerchantName(merchantName)
        totanPayPreference.storeMerchantId(merchantId)
        totanPayPreference.storeMerchantPhone(merchantPhone)
    }

    override suspend fun storeAllAccountMerchants(
        accountMerchants: List<AccountMerchant>
    ) {
        accountMerchantDao.deleteAll()
        accountMerchants.forEach {
            accountMerchantDao.insert(
                AccountMerchantEntity(
                    bankName = it.bankName,
                    farsiBankName = it.farsiBankName,
                    number = it.number,
                    isActive = it.isActive
                )
            )

        }
    }

    override suspend fun getAllAccountMerchants(): List<AccountMerchant>? {
        return accountMerchantDao.getAll()?.map {
            AccountMerchant(
                bankName = it.bankName,
                farsiBankName = it.farsiBankName,
                number = it.number,
                isActive = it.isActive,
            )
        }
    }

    override fun getMerchant(): Merchant {
        return Merchant(
            totanPayPreference.getMerchantId(),
            totanPayPreference.getMerchantPhone(),
            totanPayPreference.getMerchantName()
        )
    }


    override fun getTerminalId(): String {
        return totanPayPreference.getTerminalId() ?: ""
    }

    override fun storeAcquiringInstitutionIdentificationCode(acquiringInstitutionIdentificationCode: String) {
        totanPayPreference.storeAcquiringInstitutionIdentificationCode(
            acquiringInstitutionIdentificationCode
        )
    }

    override fun getAcquiringInstitutionIdentificationCode(): String? {
        return totanPayPreference.getAcquiringInstitutionIdentificationCode()
    }

    override fun getApportionments(): List<Apportionment>? {
        return apportionmentDao.getAll()?.map {
            Apportionment(IBAN = it.IBAN, amount = it.amount, bankName = it.bankName)
        }
    }

    override fun storeApportionments(apportionments: List<Apportionment>) {
        apportionmentDao.deleteAll()
        apportionmentDao.insertAll(apportionments.map {
            ApportionmentEntity(IBAN = it.IBAN, amount = it.amount, bankName = it.bankName)
        })
    }

    override fun getTerminalLanguage(): String {
        return "0"
    }

    override      fun getCurrency(): String {
        return "364"
    }

    override suspend fun terminalHasApportionments(): Boolean {
       return if(apportionmentDao.getAll()==null)false
        else true
    }

    override suspend fun isSetpportionments(): Boolean {
        return if(apportionmentDao.getAll()==null)false
        else true
    }
}