package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.totanpay.data.entity.TransactionQueueEntity

@Dao
interface TransactionQueueDao {
    @Insert
    suspend fun insert(transactionQueueEntity: TransactionQueueEntity)

    @Query("DELETE  FROM transaction_queue WHERE date=:date and time=:time")
    fun deleteByDateTime(date: String, time: String)

    @Query("SELECT * FROM transaction_queue WHERE date=:date and time=:time")
    fun getByDateTime(date: String, time: String): TransactionQueueEntity?

    @Query("SELECT * FROM transaction_queue")
    fun getAll(): List<TransactionQueueEntity>?
    @Query("update transaction_queue SET status=:status where date=:date and time=:time")
    fun updateStatusByDateTime(status: Char, date: String, time: String)

    @Query("SELECT * FROM transaction_queue WHERE printed=:printed ORDER BY dateTime DESC LIMIT 1\n")
    fun getLastTxnByPrinted(printed: Boolean):TransactionQueueEntity?



    @Query("update transaction_queue SET printed=:printed where date=:date and time=:time")
    fun updatePrintStatus(printed: Boolean, date: String, time: String)



}