package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import saman.zamani.persiandate.PersianDate

interface ReportRepository {
    suspend fun getLastTransaction(): ResponseTransaction?
    suspend fun geTransactionBasedStan(stan: String): ResponseTransaction?
    suspend fun getDetailsTransaction(
        fromDate:PersianDate?,
        toDate: PersianDate?,
        fromAmount: String?,
        toAmount: String?,
        selectedTransactions: String?
    ): List<ResponseTransaction>?
}
