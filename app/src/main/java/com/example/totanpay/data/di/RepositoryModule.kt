package com.example.totanpay.data.di


import com.example.totanpay.data.repository.balance.BalanceRepository
import com.example.totanpay.data.repository.balance.BalanceRepositoryImpl
import com.example.totanpay.data.repository.bill.BillRepository
import com.example.totanpay.data.repository.bill.BillRepositoryImpl
import com.example.totanpay.data.repository.configiuration.ConfigurationRepository
import com.example.totanpay.data.repository.configiuration.ConfigurationRepositoryImpl
import com.example.totanpay.data.repository.settings.connection.ConnectionRepository
import com.example.totanpay.data.repository.settings.connection.ConnectionRepositoryImpl
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.DeviceRepositoryImpl
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepositoryImpl
import com.example.totanpay.data.repository.settings.device_settings.IccCardRepository
import com.example.totanpay.data.repository.settings.device_settings.IccCardRepositoryImpl
import com.example.totanpay.data.repository.log.LogRepository
import com.example.totanpay.data.repository.log.LogRepositoryImpl
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.MainRepositoryImpl
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepositoryImpl
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepositoryImpl
import com.example.totanpay.data.repository.purchase.PurchaseRepository
import com.example.totanpay.data.repository.purchase.PurchaseRepositoryImpl
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.settings.CurrentLanguageRepositoryImpl
import com.example.totanpay.data.repository.settings.report.ReportRepository
import com.example.totanpay.data.repository.settings.report.ReportRepositoryImpl
import com.example.totanpay.data.repository.settings.supervisor.SupervisorSettingsRepository
import com.example.totanpay.data.repository.settings.supervisor.SupervisorSettingsRepositoryImpl
import com.example.totanpay.data.repository.topup.TopUpRepository
import com.example.totanpay.data.repository.topup.TopUpRepositoryImpl
import com.example.totanpay.data.repository.voucher.VoucherRepository
import com.example.totanpay.data.repository.voucher.VoucherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindIccCardRepository(repositoryImp: IccCardRepositoryImpl): IccCardRepository

    @Binds
    @Singleton
    fun bindMainRepository(repositoryImp: MainRepositoryImpl): MainRepository

    @Binds
    @Singleton
    fun bindDeviceRepository(repositoryImp: DeviceRepositoryImpl): DeviceRepository

    @Binds
    @Singleton
    fun bindConnectionRepository(repositoryImp: ConnectionRepositoryImpl): ConnectionRepository

    @Binds
    @Singleton
    fun provideReportRepository(repositoryImp: ReportRepositoryImpl): ReportRepository


    @Binds
    @Singleton
    fun providePrintCustomerSettingsRepository(printCustomerSettingsRepositoryImpl: PrintCustomerSettingsRepositoryImpl): PrintCustomerSettingsRepository


    @Binds
    @Singleton
    fun bindLogRepository(repositoryImp: LogRepositoryImpl): LogRepository

    @Binds
    @Singleton
    fun bindMerchantSettingsRepository(repositoryImp: MerchantSettingsRepositoryImpl): MerchantSettingsRepository

    @Binds
    @Singleton
    fun bindSupervisorSettingsRepository(repositoryImp: SupervisorSettingsRepositoryImpl): SupervisorSettingsRepository

    @Binds
    @Singleton
    fun bindDeviceSettingsRepository(repositoryImp: DeviceSettingsRepositoryImpl): DeviceSettingsRepository



    @Binds
    @Singleton
    fun bindBalanceRepository(repositoryImp: BalanceRepositoryImpl): BalanceRepository
    @Binds
    @Singleton
    fun bindPurchaseRepository(repositoryImp: PurchaseRepositoryImpl): PurchaseRepository
    @Binds
    @Singleton
    fun bindVoucherRepository(repositoryImp: VoucherRepositoryImpl): VoucherRepository
    @Binds
    @Singleton
    fun bindTopUpRepository(repositoryImp: TopUpRepositoryImpl): TopUpRepository
    @Binds
    @Singleton
    fun bindBillRepository(repositoryImp: BillRepositoryImpl): BillRepository

    @Binds
    @Singleton
    fun bindConfigurationRepository(repositoryImp: ConfigurationRepositoryImpl): ConfigurationRepository


    @Binds
    @Singleton
    fun bindCurrentLanguageRepository(repositoryImp: CurrentLanguageRepositoryImpl): CurrentLanguageRepository



}