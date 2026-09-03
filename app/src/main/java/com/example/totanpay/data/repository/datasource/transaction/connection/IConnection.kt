package com.example.totanpay.data.repository.datasource.transaction.connection

import com.example.totanpay.data.repository.datasource.transaction.IsoMessage
import java.io.Closeable

interface IConnection : Closeable {
    fun init(ip:String,port:Int,nii: String)
    fun start()
    fun send(msg: IsoMessage)
    fun receive(): IsoMessage?
    fun stop()
}

