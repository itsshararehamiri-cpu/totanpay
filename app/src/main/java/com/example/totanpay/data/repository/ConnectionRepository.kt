package com.example.totanpay.data.repository

interface ConnectionRepository {
    fun confirmConnectionSettings(ip: String, port: String, nii: String)
    fun getIP(): String
    fun getPort(): String
    fun getNii(): String
}