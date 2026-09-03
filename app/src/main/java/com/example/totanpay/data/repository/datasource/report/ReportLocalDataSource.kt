package com.example.totanpay.data.repository.datasource.report

import com.example.totanpay.data.repository.datasource.transaction.TransactionLog

interface ReportLocalDataSource {
    suspend fun getLastTransaction(): TransactionLog?
    suspend fun geTransactionBasedStan(stan: String,traceIsSelected: Boolean): TransactionLog?
    suspend fun a()
    suspend fun getDetailsTransaction(
        fromDate: String,
        toDate: String,
        fromAmount: String,
        toAmount: String,
        purchaseIsSelected: Boolean,
        billPayIsSelected: Boolean,
        voucherIsSelected: Boolean,
        topupIsSelected: Boolean
    ): List<TransactionLog>?

    suspend fun getDetailsTransaction(
        fromDate: Long, toDate: Long, fromAmount: String,
        toAmount: String,
        purchaseIsSelected: Boolean,
        billPayIsSelected: Boolean,
        voucherIsSelected: Boolean,
        topupIsSelected: Boolean
    ): List<TransactionLog>?

    suspend fun getMaximumAmountOfTransactions(): Long
   suspend fun clearReports()
}