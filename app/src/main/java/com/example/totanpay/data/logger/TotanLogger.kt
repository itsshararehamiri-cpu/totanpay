package com.example.totanpay.data.logger


import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TotanLogger(val clazz:Class<Any>):ILogger {
    private var logger: Logger = LoggerFactory.getLogger(clazz)
    override fun logError(error: String) {
        logger.error("info hello world$error")
    }
    override fun logInfo(error: String) {
        logger.error("error hello world")
    }
    override fun isErrorEnabled(): Boolean {
        return   logger.isErrorEnabled
    }
    override fun isDebugEnabled(): Boolean {
        return logger.isDebugEnabled
    }
    override fun debug(s: String) {
        logger.debug(s)
    }
}