package com.example.totanpay.common.ui

data class ReadCardUiState(
    val track2: String = "",
    val error: String = "",
    val merchantName: String = "",
    val englishMerchantName: String="",
    val merchantPhone: String = "",
    val isTimeOut: Boolean = false,
    val playbackSound: Boolean = false,
    val showInternetErrorMessage:Boolean=false,
    val extraMessageValue:String?=null
)