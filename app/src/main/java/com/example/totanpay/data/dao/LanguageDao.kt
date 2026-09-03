package com.example.totanpay.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.LanguageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {
    @Query("select * from language_table")
    fun getFlow(): Flow<LanguageEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: LanguageEntity)
    @Query("select * from language_table")
    fun get():LanguageEntity?
    @Update
    suspend fun update(data: LanguageEntity)
    @Query("delete  from language_table")
    fun delete()
}