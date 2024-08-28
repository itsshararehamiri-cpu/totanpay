package com.example.totanpay.common

import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

data class ResultTransactionUiState(
    val result: ResponseTransaction?=null,
    val error:String="",
    //val startPrint:Boolean=false
)