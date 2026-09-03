package com.example.totanpay.data.repository.settings
import com.example.totanpay.data.CurrentLanguage
import kotlinx.coroutines.flow.Flow

interface CurrentLanguageRepository {
    fun getLanguage():Flow<CurrentLanguage?>
    suspend fun setLanguage(isFarsiSelected:Boolean)
    suspend  fun languageIsFarsi():Boolean
}