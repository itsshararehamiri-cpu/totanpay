package com.example.totanpay.data.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun HMACSHA512(value: String): String {
    return try {
        val sh = MessageDigest.getInstance("SHA-512")
        sh.update(value.toByteArray())
        val sb = StringBuffer()
        for (b in sh.digest()) sb.append(Integer.toHexString(0xff and b.toInt()))
        sb.toString()
    } catch (e: NoSuchAlgorithmException) {
       value
    }
}
