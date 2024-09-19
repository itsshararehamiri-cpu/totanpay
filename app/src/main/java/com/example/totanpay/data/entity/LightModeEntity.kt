package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("light_mode")
data class LightModeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
   val mode:Boolean
) {

}


