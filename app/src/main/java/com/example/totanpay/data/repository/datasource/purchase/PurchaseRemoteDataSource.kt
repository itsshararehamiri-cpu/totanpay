package com.example.totanpay.data.repository.datasource.purchase

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface PurchaseRemoteDataSource {
    suspend fun purchase(
        isNetworkAvailable: () -> Boolean,
        amount: String,
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
        purchaseId: String?,
        posConditionCode: String,
        apportionments: List<Apportionment>?,
        logs: String?
    ): ResponseData<BaseTransactionResponse.PurchaseTransactionResponse>
}