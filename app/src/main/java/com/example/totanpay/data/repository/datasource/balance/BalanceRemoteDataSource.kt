package com.example.totanpay.data.repository.datasource.balance

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface BalanceRemoteDataSource {
    suspend fun balance(
        isNetworkAvailable: () -> Boolean,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        pOS: String,
        serial: String,
        appVersion: String,
        nii: String,
        posConditionCode: String
    ): ResponseData<BaseTransactionResponse.BalanceTransactionResponse>
}