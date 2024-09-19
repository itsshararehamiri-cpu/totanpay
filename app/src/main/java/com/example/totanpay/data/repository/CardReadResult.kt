package com.example.totanpay.data.repository

sealed class CardReadResult {
    data class Success(val track2: String) : CardReadResult()
    data class Error(val message: String) : CardReadResult()
    data object TimeOut : CardReadResult()
}
