package com.example.totanpay.data.repository.datasource.voucher

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface VoucherRemoteDataSource {
    suspend fun voucher(
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
        serial: String, appVersion: String, nii: String,
        productCode: String,
        posConditionCode: String,
        logs: String?,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse>
}