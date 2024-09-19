package com.example.totanpay.data.util

fun isValidIPv4(ip: String): Boolean {
    val regex = Regex("^((25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)$")
    return regex.matches(ip)
}