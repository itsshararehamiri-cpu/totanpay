package com.example.totanpay.data.di

import android.app.Application
import android.content.Context
import com.example.totanpay.data.repository.datasource.StanGenerator
import com.example.totanpay.data.repository.datasource.StanGeneratorImpl
import com.example.totanpay.data.repository.datasource.TotanPayPreference
import com.example.totanpay.data.repository.datasource.TotanPayPreferenceImpl
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.MacGeneratorImpl
import com.example.totanpay.data.repository.datasource.transaction.connection.Connection
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.repository.device.UrovoDevice
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StanGeneratorModule {
    @Binds
    @Singleton
    fun provideStanGenerator(stanGenerator: StanGeneratorImpl): StanGenerator
}

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceModule {


    @Binds
    @Singleton
    fun provideTotanPayPreference(pref: TotanPayPreferenceImpl): TotanPayPreference

}


@InstallIn(SingletonComponent::class)
@Module
object DeviceManagerModule {
    @Provides
    @Singleton
    fun provideIDevice(@ApplicationContext context: Context): IDevice {
        return UrovoDevice(context)
    }

    @Provides
    @Singleton
    fun provideIConnection(): IConnection {
        return Connection(
//           ip = "87.107.134.136", port = 4080,
//           // ip = "192.168.126.2", port = 4080,
//         // ip="192.168.149.134",port=5050,
//            packager = ISO87BPackager(),
//            header = byteArrayOf(0x60, 0x00, 0x00, 0x00, 0x00)
        )
    }


//    @Provides
//    @Singleton
//    fun provideDeviceDataManager(@ApplicationContext context: Context): IDeviceUiManager {
//        return UiDeviceManagerFactory(context).fromManufacture(android.os.Build.MANUFACTURER)
//    }
}


@Module
@InstallIn(SingletonComponent::class)
interface MacGeneratorModule {

    @Binds
    @Singleton
    fun provideIMacGenerator(repositoryImp: MacGeneratorImpl): IMacGenerator


}

//@Module
//@InstallIn(SingletonComponent::class)
//interface ConnectionModule {
//
//    @Binds
//    @Singleton
//    fun provideIConnection(repositoryImp: Connection): IConnection
//
//
//}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}