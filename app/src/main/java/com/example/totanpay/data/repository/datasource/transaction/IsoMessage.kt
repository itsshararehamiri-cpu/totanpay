package com.example.totanpay.data.repository.datasource.transaction

import android.util.Log
import com.example.totanpay.data.util.toEnglishNumber
import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOUtil
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.Charset
import java.util.Date

class Ltv {
     private val map = hashMapOf<Int, String>()
    fun addNode(key: Int, value: String): Ltv {
        map[key] = value
        return this
    }

    fun getNode(key: Int): String? {
        return if (map.containsKey(key)) map[key] else null
    }

    fun pack(): ByteArray {
        val s = StringBuilder()
        map.forEach {
            val len = it.value.length + 1
            s.append(
                String.format(
                    "%02d%02X%s".toEnglishNumber(), len, it.key, ISOUtil.hexString(
                        it.value
                            .toByteArray(charset = Charset.forName("cp1256"))
                    )
                )
            )
        }
        return ISOUtil.hex2byte(s.toString().toEnglishNumber())
    }

    fun unpack(msg: ByteArray) {
        var index = 0
        while (index < msg.size) {
            if (index + 1 >= msg.size) break
            val lenByte = msg[index].toInt() and 0xFF
            val len = (lenByte shr 4) * 10 + (lenByte and 0x0F)
            val tag = msg[index + 1].toUByte().toInt()
            val valueEnd = index + len + 1
            if (len <= 0 || valueEnd > msg.size || index + 2 > valueEnd) break
            val value = msg.copyOfRange(index + 2, valueEnd)
                .toString(charset = Charset.forName("cp1256"))
            index = valueEnd
            map[tag] = value
        }
    }
}

class IsoMessage : ISOMsg() {
    private val field48 = Ltv()
    lateinit var date: Date
        private set

    var stan: String
        get() = this.getString(11)
        set(value) = this.set(11, value)

    var processCode: String
        get() = this.getString(3)
        set(value) = this.set(3, value)

    var tranDate: String
        get() = getString(13)
        set(value) = set(13, value)
    var tranTime: String
        get() = getString(12)
        set(value) = set(12, value)

    val respCode: Int
        get() = getString(39)?.toInt() ?: -1 // TODO:

    val rrn: String?
        get() = getString(37)

    var pan: String
        get() = getString(2)
        set(value) = set(2, value)

    var amount: String
        get() = getString(4)
        set(value) = set(4, value)

    val cardIssuer: String
        get() = getField48Tag(0x38) ?: ""
    val operatorCode: String
        get() = getField48Tag(0x14) ?: ""

    fun toIsoMessage(isoMsg: ISOMsg?) {
        if (isoMsg == null)
            return
        for (field in 0..isoMsg.maxField) {
            if (isoMsg.hasField(field)) {
                try {
                    this.set(isoMsg.getComponent(field))
                } catch (e: ISOException) {
                    // it should never happen
                    println("cause${e.cause}")
                    println("message${e.message}")
                }
            }
        }
    }

    fun getDump(): String {
        val baos = ByteArrayOutputStream()
        val utf8 = "cp1256"
        PrintStream(baos, true, utf8).use { ps -> this.dump(ps, " ") }
        return baos.toString(utf8)
    }

    fun setDateTime(dateOfTransaction: String, timeOfTransaction: String) {
        this.set(12, timeOfTransaction)
        this.set(13, dateOfTransaction)
    }

    fun setSerial(serial: String) {
        field48.addNode(0x01, serial)
    }

    fun set99(field99: String) {
        field48.addNode(0x99, field99)
    }

    fun setVersion(version: String) {
        field48.addNode(0x02, version)
    }

    fun setField48(packer: () -> Unit) {
        packer()
        this.set(48, field48.pack())
    }

    fun setNii(nii: String) {
        this.set(24, nii)
    }

    fun setTerminalId(terminalId: String) {
        this.set(41, terminalId)
    }

    fun setPinBlock(pinBlock: String) {
        this.set(52, ISOUtil.hex2byte(pinBlock))
    }

    fun setCurrency(currency: String) {
        this.set(49, currency)
    }

    fun setMerchantId(merchantId: String) {
        this.set(42, merchantId)
    }

    fun setTrack2(track2: String) {
        this.set(35, track2)
    }

    fun setPOS(POS: String) {
        this.set(22, POS)// "021"
    }

    fun setBillId(billId: String) {
        field48.addNode(0x06, billId)
    }

    fun setPayId(payId: String) {
        field48.addNode(0x07, payId)
    }

    fun setTerminalLanguage(terminalLanguage: String) {
        field48.addNode(0x03, terminalLanguage)
    }

    fun setPurchaseId(purchaseId: String) {
        field48.addNode(0x09, purchaseId)
    }

    fun setTerminalConnectionType(terminalConnectionType: String) {
        field48.addNode(0x15, terminalConnectionType)
    }

    fun setTerminalType(terminalType: String) {
        field48.addNode(0x20, terminalType)
    }

    fun setProductCode(productCode: String) {
        field48.addNode(0x14, productCode)
    }

    fun setVoucherNo(voucherNo: Int) {
        field48.addNode(0x29, voucherNo.toString())
    }
    fun setMobileNumber(mobile: String) {
        field48.addNode(0x8, mobile)
    }
    fun setField48(data: ByteArray? = null) {
        if (data == null) {
            field48.unpack(this.getBytes(48))
        } else
            field48.unpack(data)
    }
    fun getField48Tag(tag: Int): String? {
        return field48.getNode(tag)
    }
}