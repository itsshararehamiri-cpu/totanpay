package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.repository.datasource.transaction.TransactionInQueue
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse


interface MainDataSource {
    suspend fun logon(
        isNetworkAvailable: () -> Boolean,
        isLoadSettings: () -> Boolean,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        posConditionCode: String,
    ): ResponseData<BaseTransactionResponse.LogonTransactionResponse>

    suspend fun init(
        isNetworkAvailable: () -> Boolean,
        terminalId: String,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        acquiringInstitutionIdentificationCode: String,
        merchantId: String,
        posConditionCode: String,
        terminalConnectionType: String
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse>

    suspend fun balance(
        isNetworkAvailable: () -> Boolean,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String,
        posConditionCode: String
    ): ResponseData<BaseTransactionResponse.BalanceTransactionResponse>

    suspend fun purchase(
        isNetworkAvailable: () -> Boolean,
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
        serial: String,
        appVersion: String,
        nii: String,
        purchaseId: String?,
        posConditionCode: String,
        apportionments: List<Apportionment>?
    ): ResponseData<BaseTransactionResponse.PurchaseTransactionResponse>


    suspend fun voucher(
        isNetworkAvailable: () -> Boolean,
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
        productCode: String,
        posConditionCode: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse>

    suspend fun topUp(
        isNetworkAvailable: () -> Boolean,
        amount: String,
        mobile: String,
        productCode: String,
        pan: String, terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String,
        posConditionCode: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.TopUpTransactionResponse>

    suspend fun billInquiry(
        isNetworkAvailable: () -> Boolean,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String,
        billID: String,
        payId: String,
        posConditionCode: String
    ): ResponseData<BaseTransactionResponse.BillInquiryTransactionResponse>

    suspend fun billPay(
        isNetworkAvailable: () -> Boolean,
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
        nii: String,
        billID: String,
        payId: String,
        serviceDesc: String?,
        currency: String,
        posConditionCode: String,
        POS: String
    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse>

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
    fun loadSettings()

}