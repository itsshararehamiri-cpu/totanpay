package com.example.totanpay.data.di

import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSourceImpl
import com.example.totanpay.data.repository.datasource.DeviceSettingsDataSource
import com.example.totanpay.data.repository.datasource.DeviceSettingsDataSourceImpl
import com.example.totanpay.data.repository.datasource.LocalAmountTransaction
import com.example.totanpay.data.repository.datasource.LocalAmountTransactionImpl
import com.example.totanpay.data.repository.datasource.LocalTaxDataSource
import com.example.totanpay.data.repository.datasource.LocalTaxDataSourceImpl
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.MainDataSourceImpl
import com.example.totanpay.data.repository.datasource.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.MerchantLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.MerchantSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.MerchantSettingsLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.PrintCustomerSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.PrintCustomerSettingsLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.ReportLocalDataSource
import com.example.totanpay.data.repository.datasource.ReportLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun provideMainDataSource(techPayPrefImp: MainDataSourceImpl): MainDataSource

    @Binds
    @Singleton
    fun provideReportLocalDataSource(reportLocalDataSourceImpl: ReportLocalDataSourceImpl): ReportLocalDataSource

    @Binds
    @Singleton
    fun provideMerchantSettingsLocalDataSource(merchantSettingsLocalDataSourceImpl: MerchantSettingsLocalDataSourceImpl): MerchantSettingsLocalDataSource


    @Binds
    @Singleton
    fun providePrintCustomerSettingsLocalDataSource(printCustomerSettingsLocalDataSourceImpl: PrintCustomerSettingsLocalDataSourceImpl): PrintCustomerSettingsLocalDataSource


    @Binds
    @Singleton
    fun provideConnectionSettingsDataSource(techPayPrefImp: ConnectionSettingsDataSourceImpl): ConnectionSettingsDataSource

    @Binds
    @Singleton
    fun provideDeviceSettingsDataSource(techPayPrefImp: DeviceSettingsDataSourceImpl): DeviceSettingsDataSource

    @Binds
    @Singleton
    fun provideMerchantLocalDataSource(
        merchantLocalDataSource: MerchantLocalDataSourceImpl
    ): MerchantLocalDataSource


    @Binds
    @Singleton
    fun provideLocalAmountTransaction(
        localAmountTransaction: LocalAmountTransactionImpl
    ): LocalAmountTransaction


    @Binds
    @Singleton
    fun provideLocalTaxDataSource(
        localTaxDataSource: LocalTaxDataSourceImpl
    ): LocalTaxDataSource

}