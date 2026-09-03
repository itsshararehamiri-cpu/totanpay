package com.example.totanpay.data.di


import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.totanpay.data.TotanPayAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val TOTANPAY_DATABASE_NAME = "totanpay_database"

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) :TotanPayAppDatabase{
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS 'log_table' (
                'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                'type' INTEGER NOT NULL,
                'number' INTEGER NOT NULL
            )
        """.trimIndent()
                )
            }
        }
        return Room.databaseBuilder(
            context,
            TotanPayAppDatabase::class.java, TOTANPAY_DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }



}

