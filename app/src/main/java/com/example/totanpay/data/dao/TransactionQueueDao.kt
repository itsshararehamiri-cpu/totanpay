package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity

@Dao
interface TransactionQueueDao{
    @Insert
    suspend fun insert(transactionQueueEntity: TransactionQueueEntity)



//    @Query("SELECT * FROM TransactionQueueEntity WHERE dateTime=:dateTime")
//    fun getWithDateTime(dateTime: String,time:String): TransactionQueueEntity?

    @Query("DELETE  FROM TransactionQueueEntity WHERE date=:date_ and time=:time")
    fun deleteByDateTime(date_: String,time:String)
//    @Query("SELECT * FROM TransactionQueue WHERE date=:date_ and time=:time")
//    fun getWithDateTime(date_: String,time:String): TransactionQueueEntity?
//
//
//    @Update
//    fun update(transactionQueue: TransactionQueueEntity)
    @Query("SELECT * FROM TransactionQueueEntity")
    fun getAll(): List<TransactionQueueEntity>?
    //abstract fun update(transactionQueue: TransactionQueueEntity)


//    @Query("update transactionqueue SET printed=:printed where date=:date_ and time=:time")
//    fun updatePrintStatus(printed: Boolean,date_: String,time: String)
//
//
//    @Query("SELECT * FROM TransactionQueueEntity WHERE printed=:printed ORDER BY dateTime DESC LIMIT 1\n")
//    fun getLastTxn(printed: Boolean):TransactionQueueEntity?


        @Query("update transactionqueueentity SET status=:status where date=:date_ and time=:time")
    fun updateStatusByDateTime(status: Char,date_: String,time: String)
}