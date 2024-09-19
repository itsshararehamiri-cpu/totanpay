package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.AccountMerchantEntity

@Dao
interface AccountMerchantDao {
    @Insert
    suspend fun insert(entity: AccountMerchantEntity)

    @Update
    suspend fun update(entity: AccountMerchantEntity)

    @Query("SELECT * FROM account_merchant ")
    fun getAll(): List<AccountMerchantEntity>?

    @Query("DELETE FROM account_merchant")
    suspend fun deleteAll()




}