package com.example.totanpay.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.totanpay.data.dao.AccountMerchantDao
import com.example.totanpay.data.dao.ApportionmentDao
import com.example.totanpay.data.dao.LanguageDao
import com.example.totanpay.data.dao.LightModeDao
import com.example.totanpay.data.dao.LogDao
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.AccountMerchantEntity
import com.example.totanpay.data.entity.ApportionmentEntity
import com.example.totanpay.data.entity.LanguageEntity
import com.example.totanpay.data.entity.LightModeEntity
import com.example.totanpay.data.entity.LogEntity
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity

@Database(
    entities = [TransactionLogEntity::class,
        TransactionQueueEntity::class,
        LightModeEntity::class,
        AccountMerchantEntity::class,
        ApportionmentEntity::class, LogEntity::class,
        LanguageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TotanPayAppDatabase : RoomDatabase() {
    abstract fun transactionLogDao(): TransactionLogDao
    abstract fun transactionQueueDao(): TransactionQueueDao
    abstract fun lightModeDao(): LightModeDao
    abstract fun accountMerchantDao(): AccountMerchantDao
    abstract fun apportionmentDao(): ApportionmentDao
    abstract fun logDao(): LogDao
    abstract fun languageDao(): LanguageDao
}