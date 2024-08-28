package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse


interface MainDataSource {
    suspend fun getKeyTransaction(
        serial: String,
        appVersion: String,
        nii: String, publicKey: String, hashCode: String
    )

    suspend fun logon(
        serial: String,
        appVersion: String,
        nii: String
    ): ResponseData<BaseTransactionResponse.LogonTransactionResponse>

    suspend fun init(
        terminalId: String,
        serial: String,
        appVersion: String,
        nii: String
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse>

    suspend fun balance(
        pan: String, terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String, appVersion: String, nii: String
    ): ResponseData<BaseTransactionResponse.BalanceTransactionResponse>

    suspend fun purchase(
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
        serial: String, appVersion: String, nii: String
    ): ResponseData<BaseTransactionResponse.PurchaseTransactionResponse>


    suspend fun voucher(
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
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse>
    suspend fun topup(
        amount: String,
        mobile:String,
        productCode:String,
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
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.TopupTransactionResponse>

    suspend fun billInquery(
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String, billID: String, payId: String
    ): ResponseData<BaseTransactionResponse.BillInqueryTransactionResponse>

    suspend fun billPay(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String, billID: String, payId: String
    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse>

    fun storeTerminalId(terminalId: String)
    fun storeMerchant(merchantId: String?, merchantPhone: String?, merchantName: String?)

    fun getMerchant(): Merchant
    fun saveConnectionSettings(ip: String, port: String, nii: String)
    fun getTerminalId(): String
    fun getLastTransaction(): TransactionLogEntity?
    fun geTransactionBasedStan(stan: String): TransactionLogEntity?
    fun getCurrency(): String
fun getTerminalConnectionType():String
    fun getTerminalType():String

    fun getNii(): String
    fun hasConnectionSettings(): Boolean
    fun getIP(): String
    fun getPort(): String
    suspend fun settlementReverse()
}