package com.example.totanpay.data.repository.datasource

import android.util.Log
import com.example.totanpay.data.dao.LanguageDao
import com.example.totanpay.data.entity.LanguageEntity
import com.example.totanpay.data.entity.toELanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LanguageLocalDataSourceImpl @Inject constructor(private val languageDao: LanguageDao) :
    LanguageLocalDataSource {
    override fun getLanguage(): Flow<ELanguage> {
        return languageDao.getFlow().map {
            it?.toELanguage() ?: ELanguage(isFarsi = true)
        }
    }

    override suspend fun setLanguage(isFarsiSelected: Boolean) {
        withContext(Dispatchers.IO){
            languageDao.delete()
            languageDao.insert(LanguageEntity(id = 1L, isFarsiSelected))
        }
    }

    override suspend fun getCurrentLanguage(): ELanguage? {
        return withContext(Dispatchers.IO){
            languageDao.get()?.toELanguage()
        }

    }
}