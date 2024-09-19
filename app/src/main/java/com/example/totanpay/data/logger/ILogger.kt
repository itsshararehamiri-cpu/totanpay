package com.example.totanpay.data.logger

interface ILogger {
    fun logError(error:String)
    fun logInfo(error:String)
    fun isErrorEnabled():Boolean
    fun isDebugEnabled():Boolean
    fun debug(s:String)
}