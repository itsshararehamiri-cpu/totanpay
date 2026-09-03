package com.example.totanpay.data.repository.datasource.configuration

import android.content.Context
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface LogonRemoteDataSource {
    suspend fun logon(context: Context,
        isNetworkAvailable: () -> Boolean,
        isLoadSettings: () -> Boolean,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        posConditionCode: String,enableMac: Boolean
    ): ResponseData<BaseTransactionResponse.LogonTransactionResponse>
}