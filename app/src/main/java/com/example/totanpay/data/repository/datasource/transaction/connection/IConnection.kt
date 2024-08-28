package com.example.totanpay.data.repository.datasource.transaction.connection

import com.example.totanpay.data.repository.datasource.transaction.IsoMessage
import java.io.Closeable

interface IConnection : Closeable {
    fun start()
    fun send(msg: IsoMessage)
    fun receive(): IsoMessage?
    fun stop()
}

