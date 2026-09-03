package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity("log_table")
data class LogEntity(val type: Int,val number: Int){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
