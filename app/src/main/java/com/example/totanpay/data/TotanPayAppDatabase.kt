package com.example.totanpay.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity


//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.example.totanpay.data.dao.TransactionLogDao
//import com.example.totanpay.data.dao.TransactionQueueDao
//import com.example.totanpay.data.entity.TransactionLogEntity
//import com.example.totanpay.data.entity.TransactionQueueEntity


@Database(
    entities = [TransactionLogEntity::class, TransactionQueueEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(
//    InstantConverter::class,
//)
abstract class TotanPayAppDatabase : RoomDatabase() {
    abstract fun transactionLogDao(): TransactionLogDao
    abstract fun transactionQueueDao(): TransactionQueueDao
}