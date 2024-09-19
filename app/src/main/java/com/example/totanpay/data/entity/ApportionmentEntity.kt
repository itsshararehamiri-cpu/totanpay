package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("apportionment")
data class ApportionmentEntity(
    val IBAN: String, val amount: String, val bankName: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long=0
}
