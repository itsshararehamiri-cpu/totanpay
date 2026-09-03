package com.example.totanpay.data.repository.log

import com.example.totanpay.data.repository.datasource.model.Log

interface LogRepository {
suspend    fun addLog(logType: LogType)
    suspend fun getAllLog():List<Log>?
    suspend fun deleteAllLog()

}
enum class LogType(val tag: Int,val lable: String) {
    KEY_CARD_INCORRECT(1,"K"),
    SUPERVISOR_PASSWORD_INCORRECT(2,"S"),
    MERCHANT_PASSWORD_INCORRECT(3,"A"),
    CHANGE_IP(4,"I"),
    CHANGE_PORT(5,"P"),
    CHANGE_NII(6,"N");

    companion object {
        fun valueOf(tag: Int): LogType? {
            var temp: LogType? = null
            for (e in LogType.entries) {
                if (e.tag == tag) {
                    temp = e
                }
            }
            return temp
        }
    }
}