package com.example.totanpay.data.repository.datasource.log

import com.example.totanpay.data.dao.LogDao
import com.example.totanpay.data.entity.LogEntity
import com.example.totanpay.data.repository.datasource.model.Log
import com.example.totanpay.data.repository.log.LogType
import com.example.totanpay.data.util.getCurrentDate
import com.example.totanpay.data.util.toEnglishNumber
import javax.inject.Inject
import kotlin.collections.forEach

class LogLocalDataSourceImpl @Inject constructor(private val logDao: LogDao) : LogLocalDataSource {
    override suspend fun saveLog(type: Int) {
        val temp = logDao.getByType(type)
        if (temp == null) {
            logDao.insert(LogEntity(type = type, number = 1))
        } else {

            val newLog = LogEntity(
                number = if (temp.number + 1 == 100) 1 else temp.number + 1,
                type = temp.type
            ).apply {
                id = temp.id
            }
            logDao.update(newLog)
        }
    }

    override suspend fun deleteAllLog() {
        logDao.deleteAll()
    }

    override suspend fun getLogs(): String? {
        val logs = getAllLog()
        if (logs == null) return null
        else {
            val temp: StringBuilder = StringBuilder()
            logs.forEach {
                temp.append(it.label.toEnglishNumber())
                temp.append(getCurrentDate().toEnglishNumber())
                if (it.number < 10)
                    temp.append("0".toEnglishNumber())
                temp.append(it.number.toString().toEnglishNumber())
            }
            return temp.toString()
        }
    }

    override suspend fun getAllLog(): List<Log>? {
        val temp = logDao.getAll()
        return temp?.map {
            Log(label = LogType.valueOf(it.type)!!.lable, number = it.number, tag = it.type)
        }
    }
}