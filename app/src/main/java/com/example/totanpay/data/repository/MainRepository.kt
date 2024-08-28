package com.example.totanpay.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse

interface MainRepository {
    suspend fun getKeyTransaction(otp: String)
    suspend fun keyInjection()
    suspend fun logon(): ResponseData<ResponseTransaction>
    suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse>
    suspend fun balance(
        track2: String,
        pinBlock: String,

        ): ResponseData<ResponseTransaction>

    suspend fun purchase(
        amount: String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction>

    suspend fun voucher(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction>
    suspend fun topup(
        amount: String,
        mobile:String,
        productCode:String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction>

    suspend fun billPay(billID: String, payId: String)
    suspend fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit)
    suspend fun getPinBlock(
        pan: String, onError: (String) -> Unit,
        onInput: (Int) -> Unit, onConfirm: (String) -> Unit,
        onCanecl: () -> Unit, onTimeOut: () -> Unit
    )

    fun confirmConnectionSettings(ip: String, port: String, nii: String)
    fun getIP(): String
    fun getPort(): String
    fun getNii(): String
    suspend fun getLastTransaction(): ResponseTransaction?
    suspend fun geTransactionBasedStan(stan: String): ResponseTransaction?
    fun isConfigured(): Boolean
     suspend fun print(bitmap: Bitmap,cotext: Context)
    suspend fun settlementReverse()
}