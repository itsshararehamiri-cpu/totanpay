package com.example.totanpay.data.repository.settings

import android.util.Log
import com.example.totanpay.data.CurrentLanguage
import com.example.totanpay.data.repository.datasource.LanguageLocalDataSource
import com.example.totanpay.data.repository.datasource.toCurrentLanguage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentLanguageRepositoryImpl @Inject constructor(    private val ioDispatcher: CoroutineDispatcher,
                                                            private val languageLocalDataSource: LanguageLocalDataSource
) :
    CurrentLanguageRepository {
    override fun getLanguage(): Flow<CurrentLanguage?> {
        return languageLocalDataSource.getLanguage().map {
            it.toCurrentLanguage()
        }
    }

    override suspend fun setLanguage(isFarsiSelected: Boolean) {
        languageLocalDataSource.setLanguage(isFarsiSelected)
    }

    override suspend fun languageIsFarsi(): Boolean {
        return withContext(ioDispatcher){
            val currentLanguage = languageLocalDataSource.getCurrentLanguage()
            currentLanguage?.isFarsi ?: true
        }
    }
}