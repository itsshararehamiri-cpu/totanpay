package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface ConfigurationRepository {
    suspend fun logon(): ResponseData<ResponseTransaction>
    suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse>
    suspend fun configuration(): ResponseData<BaseTransactionResponse.InitTransactionResponse>
}