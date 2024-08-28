package com.example.totanpay.data

import android.app.Application


import android.content.Context
import androidx.room.Room
import com.example.totanpay.data.TotanPayAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
const val TOTANPAY_DATABASE_NAME="totanpay_database"
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) =
//        Room.databaseBuilder(context, TotanPayAppDatabase::class.java, TOTANPAY_DATABASE_NAME)
//            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        Room.databaseBuilder(
            context,
            TotanPayAppDatabase::class.java, "laptops"
        ).build()
    @Provides
    @Singleton
    fun provideTransactionLogDao(database: TotanPayAppDatabase) = database.transactionLogDao()

    @Provides
    @Singleton
    fun provideTransactionQueueDao(database: TotanPayAppDatabase) = database.transactionQueueDao()


}

