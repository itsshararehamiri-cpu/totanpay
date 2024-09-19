package com.example.totanpay.data.repository

import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

interface TopUpRepository {
    suspend fun topUp(
        amount: String,
        mobile: String,
        operator: Operator,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction>

    fun  getAmountWithTax(amount: String, operator: Operator): String
    fun getTaxForCharge():String
}