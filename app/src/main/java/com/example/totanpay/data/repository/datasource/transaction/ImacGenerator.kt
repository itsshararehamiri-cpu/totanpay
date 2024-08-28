package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.IDevice
import org.jpos.iso.ISOUtil
import javax.inject.Inject


interface IMacGenerator {
    fun getMac(msg: IsoMessage): ByteArray

}

class MacGeneratorImpl @Inject constructor(private val device: IDevice) : IMacGenerator {
    override fun getMac(msg: IsoMessage): ByteArray {
        val macField = if (msg.maxField > 64) 128 else 64
        msg.set(macField, "0000000000000000")
        with (msg.pack().dropLast(8).toByteArray()){
            val mac = device.getMac(this)
            val macB =  ISOUtil.hexString(mac).substring(0, 8).toByteArray()
            msg.set(macField, macB)
            return macB
        }
    }
}
