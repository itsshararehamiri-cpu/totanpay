package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_merchant"
)
data class AccountMerchantEntity(

    var bankName: String? = null,
    var farsiBankName: String? = null,
    var number: String? = null,
    var isActive: Boolean = true
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
