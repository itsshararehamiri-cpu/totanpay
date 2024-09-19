package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

interface BillRepository {
    suspend fun billInquiry(
        billID: String,
        payId: String
    ): ResponseData<ResponseTransaction>

    suspend fun billPay(
        billID: String,
        payId: String,
        amount: String,
        track2: String,
        pinBlock: String, serviceDesc: String?
    ): ResponseData<ResponseTransaction>

}