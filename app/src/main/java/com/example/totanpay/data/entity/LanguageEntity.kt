package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.totanpay.data.repository.datasource.ELanguage


@Entity(
    tableName = "language_table"
)
data class LanguageEntity(
    @PrimaryKey(autoGenerate = true) var id: Long, val isFarsiSelected: Boolean
)

fun LanguageEntity.toELanguage(): ELanguage {
    return ELanguage(isFarsi = this.isFarsiSelected)
}