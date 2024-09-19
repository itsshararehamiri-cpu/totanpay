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

    @Query("SELECT * FROM transaction_log WHERE type!=:exceptType and responseCode=:responseCode ORDER BY id DESC LIMIT 1\n")
    fun getLast(exceptType: Int, responseCode: Int): TransactionLogEntity?//, respCode: Int?


    @Query("SELECT * FROM transaction_log WHERE stan=:stan")
    fun getByStan(stan: String): TransactionLogEntity?//, respCode: Int?

    @Update
    fun update(transactionLog: TransactionLogEntity)


    @Query("SELECT * FROM transaction_log WHERE dateTransaction=:date_ and timeTransaction=:time")
    fun getByDateTime(date_: String, time: String): TransactionLogEntity?


    @Query("SELECT * FROM transaction_log WHERE dateTransaction<=:fromDate and dateTransaction>:toDate" +
            " and amount<=:fromAmount and amount>:toAmount and type IN (:selectedTransactions)")
    fun getInRangeDate(
        fromDate: String,
        toDate: String,
        fromAmount: Long,
        toAmount: Long,
        selectedTransactions: List<Int>,
    ): List<TransactionLogEntity>?


//    @Query("SELECT * FROM transaction_log WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC LIMIT 1\n")
//    suspend fun getInRangeDate(from: Long, to: Long): List<TransactionLogEntity>


//    @Query("SELECT * FROM transaction_log WHERE timestamp>:from AND timestamp<:to ORDER BY timestamp "+
//            " and amount<=:fromAmount and amount>:toAmount and type IN (:selectedTransactions)")
//    suspend fun getInRangeDate(from: Long, to: Long,
//                               fromAmount: Long,
//                               toAmount: Long,
//                               selectedTransactions: List<Int>,): List<TransactionLogEntity>

    @Query("SELECT * FROM transaction_log WHERE  type IN (:selectedTransactions)")
    suspend fun getInRangeDate(
        selectedTransactions: List<Int>,): List<TransactionLogEntity>


    @Query("SELECT * FROM transaction_log \n" +
            "    WHERE timestamp >= :from \n" +
            "    AND timestamp <= :to \n" +
            "    AND amount >= :fromAmount \n" +
            "    AND amount <= :toAmount \n" +
            "    AND type IN (:selectedTransactions) \n" +
            "    ORDER BY timestamp DESC")
    suspend fun getInRangeDate(from: Long, to: Long,
                               fromAmount: Long,
                               toAmount: Long,
                               selectedTransactions: List<Int>,): List<TransactionLogEntity>


    @Query("SELECT MAX(amount) FROM transaction_log")
    suspend fun getMaxAmount(): Long?

    @Query("SELECT * FROM transaction_log")
    fun getAll():List<TransactionLogEntity>

    @Query("DELETE  FROM transaction_log WHERE dateTransaction=:date and timeTransaction=:time")
    fun deleteByDateTime(date: String, time: String)
}