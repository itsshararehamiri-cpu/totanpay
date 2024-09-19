package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.LightModeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LightModeDao {
    @Insert
    suspend fun insert(mode: LightModeEntity)

    @Update
    suspend fun update(mode: LightModeEntity)

    @Query("SELECT * FROM light_mode  LIMIT 1")
    fun getFlow(): Flow<LightModeEntity>?


    @Query("SELECT * FROM light_mode  LIMIT 1")
    fun get():LightModeEntity?
}