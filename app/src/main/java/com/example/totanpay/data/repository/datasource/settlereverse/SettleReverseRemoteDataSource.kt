package com.example.totanpay.data.repository.datasource.settlereverse

import com.example.totanpay.data.repository.datasource.transaction.TransactionInQueue
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog

interface SettleReverseRemoteDataSource {
  suspend  fun sendTransactionInQueue(
    terminalLanguage: String,
    terminalConnectionType: String,
    terminalType: String
    ): Boolean
    suspend fun settlementReverse(
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String
    )

    suspend fun getTransactionInQueue(
        dateOfTransaction: String,
        timeOfTransaction: String
    ): TransactionInQueue?

    suspend fun updatePrintStatusOfTransactionInQueue(transactionInQueue: TransactionInQueue?)
    suspend fun updatePrintStatusOfTransactionInQueue(printStatus: Boolean)
    suspend fun getLastTxnIsNotPrinted(): TransactionLog?
    suspend fun hasTransactionInQueue(): Boolean
}