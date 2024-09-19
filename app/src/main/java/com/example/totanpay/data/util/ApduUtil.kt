package com.example.totanpay.data.util

import android.util.Log
import org.jpos.iso.ISOException
import org.jpos.iso.ISOUtil
import java.security.PrivateKey
import javax.crypto.Cipher


object ApduUtil {
    fun apduResponseIsSuccess(responseBuffer: ByteArray): Boolean {
        return ISOUtil.hexString(responseBuffer).takeLast(4) == "9000"
    }

    fun getSelectFirstAppletCommand(): String {
        return "00A40400" + "08" + "0000504E414B4341"
    }

    fun getSelectSecondAppletCommand(): String {
        return "00A40400" + "08" + "0000504E414B4353"
    }

    @Throws
    fun getVerifyFirstPinCommand(pin: String): String {
        try {
            val pIN = Integer.toHexString(pin.toInt())
            return "AC200000" + ISOUtil.padleft((pIN.length / 2).toString() + "", 2, '0') + pIN
        } catch (e: ISOException) {
            throw RuntimeException(e)
        }
    }

    fun getReadMasterKeyCommand(): String {
        return "5CBD0100FF"
    }

    @Throws
    fun getReadFactoryKeyCommand(pin: String): String {
        return "5CBD0000FF"
    }

    @Throws(Exception::class)
    fun decrypt(publicKey: PrivateKey?, encrypted: ByteArray?): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        return cipher.doFinal(encrypted)
    }

    fun getVerifySecondPinCommand(pin: String): String? {
        var PIN = Integer.toHexString(pin.toInt())
        try {
            PIN = ISOUtil.padleft(PIN, 4, '0')
        } catch (e: ISOException) {
            Log.d("", "ISOException: ${e.cause}")
        }
        try {
            return "5C200000" + ISOUtil.padleft(
                (PIN.length / 2).toString() + "",
                2,
                '0'
            ) + PIN
        } catch (e: ISOException) {
            Log.d("", "getVerifySecondPinCommand: ${e.cause}")
        }
        return null
    }

    fun getReadPublicKeyCommand(): String {
        return "ACBD000003"
    }

    fun getExponentCommand(): String {
        return "ACBD000180"
    }

    fun getReadPrivateKeyCommand(): String {
        return "ACBD010080"
    }
}