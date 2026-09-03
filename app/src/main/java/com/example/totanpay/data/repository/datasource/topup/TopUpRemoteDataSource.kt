package com.example.totanpay.data.repository.datasource.topup

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface TopUpRemoteDataSource {
    suspend fun topUp(
        isNetworkAvailable: () -> Boolean,
        amount: String,
        mobile: String,
        productCode: String,
        pan: String, terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String,
        posConditionCode: String,
        logs: String?,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.TopUpTransactionResponse>
}