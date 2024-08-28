package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.TransactionLogEntity

@Dao
interface TransactionLogDao {
    @Insert
    suspend fun insert(tranData: TransactionLogEntity)

    @Query("SELECT * FROM tran_log WHERE type!=:exceptType ORDER BY id DESC LIMIT 1\n")
    fun getLast(exceptType: Int): TransactionLogEntity?//, respCode: Int?


    @Query("SELECT * FROM tran_log WHERE stan=:stan")
    fun getByStan(stan:String): TransactionLogEntity?//, respCode: Int?

    @Update
    fun update(transactionLog: TransactionLogEntity)


    @Query("SELECT * FROM tran_log WHERE dateTransaction=:date_ and timeTransaction=:time")
    fun getByDateTime(date_: String, time: String): TransactionLogEntity?
}