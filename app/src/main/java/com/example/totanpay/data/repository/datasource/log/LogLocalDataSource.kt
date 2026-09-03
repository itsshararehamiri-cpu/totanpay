package com.example.totanpay.data.repository.datasource.log

import com.example.totanpay.data.repository.datasource.model.Log

interface LogLocalDataSource {
    suspend fun saveLog(type: Int)
    suspend fun getAllLog():List<Log>?
    suspend fun deleteAllLog()
    suspend fun getLogs(): String?
}