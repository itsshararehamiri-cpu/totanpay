package com.example.totanpay.data.repository.datasource

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun String.formatAmount(): String {
    val t = this.replace(",", "")
    return String.format("%,d", t.toLong())
}

fun String?.mask(): String {
    return if (this != null)
        "${take(6)}****${takeLast(4)}" else ""
}

fun getSwitchMessage(code: Int): String {
    return when (code) {
        0 -> "تراکنش موفق"

        else -> "خطای نامشخص"
    }
}

fun getDateOfTransaction(): String {
    val date: Date = Date()
    val df = SimpleDateFormat("MMdd", Locale.US)
    return df.format(date)
}

fun getTimeOfTransaction(): String {
    val date: Date = Date()
    val tf = SimpleDateFormat("HHmmss", Locale.US)
    return tf.format(date)
}
fun toHex(arg: String): String {
    return String.format("%040x", BigInteger(1, arg.toByteArray()))
}
fun getDateTimeInGMT(): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(calendar.time)
}

fun getSHA256(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.toHexString()
}
fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
fun g():String{
    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("MMddHHmmss", Locale.US)
    val field7Value = dateFormat.format(currentDateTime)
return field7Value
    // Replace the field 7 value in the ISO 8583 message
//    return isoMessage.replaceRange(6, 12, field7Value)
}

fun getSHA512(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-512")
    val digest = md.digest(bytes)
    return digest.toHexString()
}

