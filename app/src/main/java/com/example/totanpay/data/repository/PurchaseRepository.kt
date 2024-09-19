package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

interface PurchaseRepository {
    suspend fun purchase(
        amount: String,
        track2: String,
        pinBlock: String,
        purchaseId: String?
    ): ResponseData<ResponseTransaction>

}