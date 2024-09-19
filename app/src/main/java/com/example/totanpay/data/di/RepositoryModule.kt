package com.example.totanpay.data.di


import com.example.totanpay.data.repository.BalanceRepository
import com.example.totanpay.data.repository.BalanceRepositoryImpl
import com.example.totanpay.data.repository.BillRepository
import com.example.totanpay.data.repository.BillRepositoryImpl
import com.example.totanpay.data.repository.ConfigurationRepository
import com.example.totanpay.data.repository.ConfigurationRepositoryImpl
import com.example.totanpay.data.repository.ConnectionRepository
import com.example.totanpay.data.repository.ConnectionRepositoryImpl
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.DeviceRepositoryImpl
import com.example.totanpay.data.repository.DeviceSettingsRepository
import com.example.totanpay.data.repository.DeviceSettingsRepositoryImpl
import com.example.totanpay.data.repository.IccCardRepository
import com.example.totanpay.data.repository.IccCardRepositoryImpl
import com.example.totanpay.data.repository.LogRepository
import com.example.totanpay.data.repository.LogRepositoryImpl
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.MainRepositoryImpl
import com.example.totanpay.data.repository.MerchantSettingsRepository
import com.example.totanpay.data.repository.MerchantSettingsRepositoryImpl
import com.example.totanpay.data.repository.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.PrintCustomerSettingsRepositoryImpl
import com.example.totanpay.data.repository.PurchaseRepository
import com.example.totanpay.data.repository.PurchaseRepositoryImpl
import com.example.totanpay.data.repository.ReportRepository
import com.example.totanpay.data.repository.ReportRepositoryImpl
import com.example.totanpay.data.repository.SupervisorSettingsRepository
import com.example.totanpay.data.repository.SupervisorSettingsRepositoryImpl
import com.example.totanpay.data.repository.TopUpRepository
import com.example.totanpay.data.repository.TopUpRepositoryImpl
import com.example.totanpay.data.repository.VoucherRepository
import com.example.totanpay.data.repository.VoucherRepositoryImpl
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
}