package com.example.totanpay.data.util

fun isValidPhoneNumber(phone: String): Boolean {
    val cleaned = phone.replace("\\s|-|\\(|\\)".toRegex(), "")
    val regex = Regex("^(\\+98|0)?9\\d{9}\$")
    return regex.matches(cleaned)
}
