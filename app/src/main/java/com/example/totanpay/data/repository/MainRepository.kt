package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.response.AccountMerchant
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface MainRepository {
    suspend fun isConfigured(): Boolean
    suspend fun print(
        bitmap: Bitmap,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    )

    suspend fun settlementReverse()

    suspend fun changePrintStatusOfTransactionInQueue(
        dateOfTransaction: String,
        timeOfTransaction: String
    )

    suspend fun getMerchant(): Merchant?
    suspend fun getTerminalId(): String?
    suspend fun getNotPrintLastTransactionInQueue(): ResponseTransaction?

    suspend fun getAllAccountMerchants(): List<AccountMerchant>?
    fun isNetworkAvailable(): Boolean
    fun setTaxIrancellCharge(tax:String)
    fun getTaxIrancellCharge():String

}