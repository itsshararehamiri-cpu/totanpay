package com.example.totanpay.data.repository.datasource.configuration

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface TerminalInfoRemoteDataSource {
    suspend fun init(
        isNetworkAvailable: () -> Boolean,
        terminalId: String,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        acquiringInstitutionIdentificationCode: String,
        merchantId: String,
        posConditionCode: String,
        terminalConnectionType: String
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse>
}