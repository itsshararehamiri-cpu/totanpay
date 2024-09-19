package com.example.totanpay.data
object ConnectionException: Exception() {
    override val message: String
        get() = "امکان انجام تراکنش وجود ندارد لطفا اینترنت را بررسی کنید"
}
