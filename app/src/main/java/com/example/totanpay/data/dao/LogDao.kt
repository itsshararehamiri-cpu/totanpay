package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.LightModeEntity
import com.example.totanpay.data.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert
    suspend fun insert(logEntity: LogEntity)

    @Update
    suspend fun update(logEntity: LogEntity)

    @Query("SELECT * FROM log_table  LIMIT 1")
    suspend fun get(): LogEntity?


    @Query("SELECT * FROM log_table")
    suspend fun getAll():List< LogEntity>?

    @Query("DELETE  FROM log_table WHERE type=:type")
    suspend fun delete(type: String)


    @Query("SELECT * FROM log_table  WHERE type=:type")
    suspend fun getByType(type: Int): LogEntity?


    @Query("DELETE  FROM log_table")
    suspend fun deleteAll()
}