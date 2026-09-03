package com.example.totanpay.data.repository.datasource.bill

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface BillRemoteDataSource {
    suspend fun billInquiry(
        isNetworkAvailable: () -> Boolean,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String,
        billID: String,
        payId: String,
        posConditionCode: String
    ): ResponseData<BaseTransactionResponse.BillInquiryTransactionResponse>

    suspend fun billPay(
        isNetworkAvailable: () -> Boolean,
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String,
        billID: String,
        payId: String,
        serviceDesc: String?,
        currency: String,
        posConditionCode: String,
        pOS: String,
        logs: String?
    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse>
}