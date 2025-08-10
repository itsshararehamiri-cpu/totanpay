package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.device.IDevice
import com.example.totanpay.data.util.toEnglishNumber
import org.jpos.iso.ISOUtil
import javax.inject.Inject


interface IMacGenerator {
    fun getMac(msg: IsoMessage, index: Int = 1): ByteArray?

}

class MacGeneratorImpl @Inject constructor(private val device: IDevice) : IMacGenerator {
    override fun getMac(msg: IsoMessage, index: Int): ByteArray {
        val macField = (if (msg.maxField > 64) 128 else 64).toString().toEnglishNumber()
        msg.set(macField, "0000000000000000".toEnglishNumber())
        with(msg.pack().dropLast(8).toByteArray()) {
            val mac = device.getMac(this, index)
            val macB = ISOUtil.hexString(mac).toEnglishNumber().substring(0, 8).toEnglishNumber()
                .toByteArray()
            msg.set(macField, macB)
            return macB
        }
    }
}
