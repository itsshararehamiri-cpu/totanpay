package com.example.totanpay.common

import com.example.totanpay.data.repository.PrintStatus
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

data class ResultTransactionUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val printStatus: PrintStatus = PrintStatus.ALWAYS_PRINTING,
    val responseForCallerApp: String? = null,
    val playbackSound: Boolean = false
)