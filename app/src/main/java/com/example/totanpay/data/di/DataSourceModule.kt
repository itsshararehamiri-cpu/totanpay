package com.example.totanpay.data.di


import com.example.totanpay.data.repository.datasource.LanguageLocalDataSource
import com.example.totanpay.data.repository.datasource.LanguageLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.MainDataSourceImpl
import com.example.totanpay.data.repository.datasource.balance.BalanceRemoteDataSource
import com.example.totanpay.data.repository.datasource.balance.BalanceRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.bill.BillRemoteDataSource
import com.example.totanpay.data.repository.datasource.bill.BillRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.configuration.LogonRemoteDataSource
import com.example.totanpay.data.repository.datasource.configuration.LogonRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.configuration.TerminalInfoRemoteDataSource
import com.example.totanpay.data.repository.datasource.configuration.TerminalInfoRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.purchase.PurchaseRemoteDataSource
import com.example.totanpay.data.repository.datasource.purchase.PurchaseRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.report.ReportLocalDataSource
import com.example.totanpay.data.repository.datasource.report.ReportLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.DeviceSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.DeviceSettingsDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.LocalAmountTransaction
import com.example.totanpay.data.repository.datasource.settings.LocalAmountTransactionImpl
import com.example.totanpay.data.repository.datasource.settings.LocalTaxDataSource
import com.example.totanpay.data.repository.datasource.settings.LocalTaxDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.MacEnableLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MacEnableLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.MerchantSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantSettingsLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.settings.PrintCustomerSettingsLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.PrintCustomerSettingsLocalDataSourceImpl
import com.example.totanpay.data.repository.datasource.settlereverse.SettleReverseRemoteDataSource
import com.example.totanpay.data.repository.datasource.settlereverse.SettleReverseRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.topup.TopUpRemoteDataSource
import com.example.totanpay.data.repository.datasource.topup.TopUpRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.voucher.VoucherRemoteDataSource
import com.example.totanpay.data.repository.datasource.voucher.VoucherRemoteDataSourceImpl
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
    fun bindMainDataSource(techPayPrefImp: MainDataSourceImpl): MainDataSource

    @Binds
    @Singleton
    fun bindReportLocalDataSource(reportLocalDataSourceImpl: ReportLocalDataSourceImpl): ReportLocalDataSource

    @Binds
    @Singleton
    fun bindMerchantSettingsLocalDataSource(merchantSettingsLocalDataSourceImpl: MerchantSettingsLocalDataSourceImpl): MerchantSettingsLocalDataSource


    @Binds
    @Singleton
    fun bindPrintCustomerSettingsLocalDataSource(printCustomerSettingsLocalDataSourceImpl: PrintCustomerSettingsLocalDataSourceImpl): PrintCustomerSettingsLocalDataSource


    @Binds
    @Singleton
    fun bindConnectionSettingsDataSource(techPayPrefImp: ConnectionSettingsDataSourceImpl): ConnectionSettingsDataSource

    @Binds
    @Singleton
    fun bindDeviceSettingsDataSource(techPayPrefImp: DeviceSettingsDataSourceImpl): DeviceSettingsDataSource

    @Binds
    @Singleton
    fun bindMerchantLocalDataSource(
        merchantLocalDataSource: MerchantLocalDataSourceImpl
    ): MerchantLocalDataSource


    @Binds
    @Singleton
    fun bindLocalAmountTransaction(
        localAmountTransaction: LocalAmountTransactionImpl
    ): LocalAmountTransaction


    @Binds
    @Singleton
    fun bindLocalTaxDataSource(
        localTaxDataSource: LocalTaxDataSourceImpl
    ): LocalTaxDataSource

    @Binds
    @Singleton
    fun bindLogLocalDataSource(
        logLocalDataSource: LogLocalDataSourceImpl
    ): LogLocalDataSource


    @Binds
    @Singleton
    fun bindMacEnableLocalDataSource(impl: MacEnableLocalDataSourceImpl): MacEnableLocalDataSource


    @Binds
    @Singleton
    fun bindBalanceRemoteDataSource(impl: BalanceRemoteDataSourceImpl): BalanceRemoteDataSource

    @Binds
    @Singleton
    fun bindBillRemoteDataSource(impl: BillRemoteDataSourceImpl): BillRemoteDataSource

    @Binds
    @Singleton
    fun bindPurchaseRemoteDataSource(impl: PurchaseRemoteDataSourceImpl): PurchaseRemoteDataSource

    @Binds
    @Singleton
    fun bindVoucherRemoteDataSource(impl: VoucherRemoteDataSourceImpl): VoucherRemoteDataSource


    @Binds
    @Singleton
    fun bindSettleReverseRemoteDataSource(impl: SettleReverseRemoteDataSourceImpl): SettleReverseRemoteDataSource

    @Binds
    @Singleton
    fun bindLogonRemoteDataSource(impl: LogonRemoteDataSourceImpl): LogonRemoteDataSource


    @Binds
    @Singleton
    fun bindTerminalInfoRemoteDataSource(impl: TerminalInfoRemoteDataSourceImpl): TerminalInfoRemoteDataSource

    @Binds
    @Singleton
    fun bindTopUpRemoteDataSource(impl:TopUpRemoteDataSourceImpl): TopUpRemoteDataSource


    @Binds
    @Singleton
    fun bindLanguageLocalDataSource(impl: LanguageLocalDataSourceImpl): LanguageLocalDataSource

}