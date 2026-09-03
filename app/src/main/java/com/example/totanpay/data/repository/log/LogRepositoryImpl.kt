package com.example.totanpay.data.repository.log

import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.model.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(private val logLocalDataSource: LogLocalDataSource,
                                            private val ioDispatcher: CoroutineDispatcher):LogRepository {
    override suspend fun addLog(log: LogType) {
        withContext(ioDispatcher){
            logLocalDataSource.saveLog(log.tag)
        }
    }

    override suspend fun getAllLog(): List<Log>? {
        return withContext(ioDispatcher){
            logLocalDataSource.getAllLog()
        }
    }

    override suspend fun deleteAllLog() {
        return withContext(ioDispatcher){
            logLocalDataSource.deleteAllLog()
        }
    }
}