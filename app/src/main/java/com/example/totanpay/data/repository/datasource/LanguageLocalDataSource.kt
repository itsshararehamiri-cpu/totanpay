package com.example.totanpay.data.repository.datasource

import kotlinx.coroutines.flow.Flow

interface LanguageLocalDataSource {
    fun getLanguage(): Flow<ELanguage>
    suspend fun setLanguage(isFarsiSelected: Boolean)
    suspend fun getCurrentLanguage(): ELanguage?
}