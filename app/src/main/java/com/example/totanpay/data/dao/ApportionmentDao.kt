package com.example.totanpay.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.totanpay.data.entity.ApportionmentEntity

@Dao
interface ApportionmentDao {
    @Insert
    suspend fun insert(apportionmentEntity: ApportionmentEntity)

    @Update
    suspend fun update(apportionmentEntity: ApportionmentEntity)


    @Query("SELECT * FROM apportionment")
    fun getAll(): List<ApportionmentEntity>?

    @Query("DELETE  FROM apportionment")
    fun deleteAll()

    @Insert
     fun insertAll(apportionmentEntitys: List<ApportionmentEntity>)
}