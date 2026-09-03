package com.example.totanpay.data.repository.voucher

import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

interface VoucherRepository {
    suspend fun voucher(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,
        operator: Operator
    ): ResponseData<ResponseTransaction>
}