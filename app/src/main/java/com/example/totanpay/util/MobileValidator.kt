package com.example.totanpay.util

fun isValidIranianMobileNumber(phone: String): Boolean {
    val regex = Regex("""^(\+98|0)?9(0[0-5]|1[0-9]|2[0-2]|3[0-9]|9[0-9]|4[0-9]|8[0-9])\d{7}$""")
    return regex.matches(phone)
}
