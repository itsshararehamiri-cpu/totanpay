package com.example.totanpay.data.repository.datasource

import android.util.Base64
import com.example.totanpay.data.util.toEnglishNumber
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun String.toHexString(): String {
    return this.map { it.code } // Convert each character to its ASCII integer value
        .joinToString("") { String.format("%02x", it) } // Format each integer to a two-digit hex string
}
fun String.formatAmount(): String {
    val t = this.replace(",", "")
    return String.format("%,d", t.toEnglishNumber().toLong())
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
    // TODO: get from sdk 
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
fun generateDateTimeInGMT():String{
    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("MMddHHmmss", Locale.US)
    val field7Value = dateFormat.format(currentDateTime)
return field7Value
    // Replace the field 7 value in the ISO 8583 message
//    return isoMessage.replaceRange(6, 12, field7Value)
}
fun generateSHA1(input: ByteArray): String {
    // Create a SHA-1 MessageDigest instance
    val digest = MessageDigest.getInstance("SHA-1")

    // Update the digest with the input string's bytes
    val hashBytes = digest.digest(input)

    // Convert the byte array to a hexadecimal string
    return hashBytes.joinToString("") { String.format("%02x", it) }
}
fun getSHA512(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-512")
    val digest = md.digest(bytes)
    return digest.toHexString()
}

@Throws(Exception::class)
fun decryptUsing3Des(encryptedText: String, secretKey: String): String {
    val ALGO = "DESede/CBC/PKCS7Padding"
    //val ALGO = "DESede/ECB/PKCS7Padding"
    val message: ByteArray = Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT)

    val decipher = Cipher.getInstance(ALGO)
    decipher.init(Cipher.DECRYPT_MODE, getSecreteKey(secretKey))

    val plainText = decipher.doFinal(message)

    return String(plainText, charset("UTF-8"))
}

fun gg(encryptedText: String, secretKey: String):String{
// Define the algorithm and mode (CBC requires IV)
    val ALGO = "DESede/CBC/PKCS7Padding"

// Base64 decode the encrypted message
    val message: ByteArray = Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT)

// Extract the IV (this assumes the IV is prepended to the ciphertext, adjust as needed)
    val ivSize = 8 // DESede (3DES) uses an 8-byte (64-bit) IV
    val ivBytes = message.copyOfRange(0, ivSize)  // First 8 bytes are IV
    val ciphertext = message.copyOfRange(ivSize, message.size)  // Rest is ciphertext

// Create IvParameterSpec from the IV
    val ivSpec = IvParameterSpec(ivBytes)

// Your secret key should be exactly 24 bytes for DESede (168-bit key)
    val secretKeyBytes = Base64.decode(secretKey.toByteArray(), Base64.DEFAULT)
    val secretKeySpec = SecretKeySpec(secretKeyBytes, "DESede")

// Initialize the cipher for decryption using the key and IV
    val decipher = Cipher.getInstance(ALGO)
    decipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

// Decrypt the ciphertext
    val plainText: ByteArray
    try {
        plainText = decipher.doFinal(ciphertext)
    } catch (e: Exception) {
        // Handle decryption errors (like padding issues, wrong key, etc.)
        throw RuntimeException("Decryption failed", e)
    }

// Return the plaintext as a string
    return String(plainText, charset("UTF-8"))

}
@Throws(java.lang.Exception::class)
fun getSecreteKey(secretKey: String): SecretKey {
    val md = MessageDigest.getInstance("SHA-1")
    val digestOfPassword = md.digest(secretKey.toByteArray(charset("utf-8")))
    val keyBytes = digestOfPassword.copyOf(24)
    val key: SecretKey = SecretKeySpec(keyBytes, "DESede")
    return key
}
 fun parseSharedKey(key: String): String {
    val sb = StringBuilder()
    var i = 0
    while (i < key.length) {
        if (i + 1 < key.length) {
            sb.insert(0, key.substring(i, i + 2))
        } else {
            sb.insert(0, key[i])
        }
        i += 2
    }
    return sb.toString()
}
fun convertString(input: String): String {
    val utf8 = "cp1256"
    // Try decoding with the proper charset, e.g. ISO-8859-1 (Latin-1)
    val originalBytes = input.toByteArray(Charset.forName("ISO-8859-1"))

    // Now decode with UTF-8
    return String(originalBytes, Charset.forName(utf8))
}
