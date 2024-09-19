package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

interface BalanceRepository {

    suspend fun balance(
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction>
}