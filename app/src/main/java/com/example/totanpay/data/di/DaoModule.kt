package com.example.totanpay.data.di

import com.example.totanpay.data.TotanPayAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideTransactionLogDao(database: TotanPayAppDatabase) = database.transactionLogDao()

    @Provides
    @Singleton
    fun provideTransactionQueueDao(database: TotanPayAppDatabase) = database.transactionQueueDao()


    @Provides
    @Singleton
    fun provideLightModeDao(database: TotanPayAppDatabase) = database.lightModeDao()

    @Provides
    @Singleton
    fun provideAccountMerchantDao(database: TotanPayAppDatabase) = database.accountMerchantDao()

    @Provides
    @Singleton
    fun provideApportionmentDao(database: TotanPayAppDatabase) = database.apportionmentDao()


    @Provides
    @Singleton
    fun provideLogDao(database: TotanPayAppDatabase) = database.logDao()


    @Provides
    @Singleton
    fun provideLanguageDao(database: TotanPayAppDatabase) = database.languageDao()
}