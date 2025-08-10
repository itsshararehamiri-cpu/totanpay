package com.example.totanpay.data.repository.datasource

import androidx.multidex.BuildConfig
import com.example.totanpay.data.ConnectionException
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity
import com.example.totanpay.data.entity.toTransaction
import com.example.totanpay.data.entity.toTransactionInQueue
import com.example.totanpay.data.repository.datasource.transaction.BalanceTransaction
import com.example.totanpay.data.repository.datasource.transaction.BillInquiryTransaction
import com.example.totanpay.data.repository.datasource.transaction.BillPayTransaction
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.InitTransaction
import com.example.totanpay.data.repository.datasource.transaction.LogonTransaction
import com.example.totanpay.data.repository.datasource.transaction.PurchaseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.SettlementReverseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TopUpTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionInQueue
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.VoucherTransaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.request.BalanceTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.BillInquiryTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.BillPayTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.InitTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.PurchaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.SettlementReverseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.TopUpTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.VoucherTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBalanceTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBillInquiryTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBillPayTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toInitTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toLogonTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toPurchaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toTopupTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toVoucherTransactionResponse
import com.example.totanpay.data.util.toEnglishNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainDataSourceImpl @Inject constructor(
    private val macGenerator: IMacGenerator,
    private val connection: IConnection,
    private val transactionLogDao: TransactionLogDao,
    private val stanGenerator: StanGenerator,
    private val transactionQueueDao: TransactionQueueDao,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource
) : MainDataSource {
    init {
        loadSettings()
    }

    override fun loadSettings() {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt()
            )
        }
    }

    override suspend fun logon(
        isNetworkAvailable: () -> Boolean,
        isLoadSettings: () -> Boolean,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        posConditionCode: String): ResponseData<BaseTransactionResponse.LogonTransactionResponse> {
        if (isNetworkAvailable()) {
            if (isLoadSettings()) {
                val transaction = LogonTransaction(
                    LogonTransactionRequest(
                        stan = generateStan().toString().toEnglishNumber().toInt(),
                        serial = serial.toEnglishNumber(),
                        appVersion = appVersion,
                        nii = nii.toEnglishNumber(),
                        date = getDateOfTransaction().toEnglishNumber(),
                        time = getTimeOfTransaction().toEnglishNumber(),
                        terminalLanguage = terminalLanguage.toEnglishNumber(),
                        terminalConnectionType = terminalConnectionType.toEnglishNumber(),
                        posConditionCode = posConditionCode.toEnglishNumber()
                    ), macGenerator, connection
                )
                val response = transaction.execute()
                return if (response is FailedTransactionResponse) {
                    ResponseData.Error(
                        data = response.toLogonTransactionResponse(
                            getResponseMessageFromResponseCode(
                                response.responseCode, response.responseMessage
                            )
                        ),
                        error = getExceptionFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    )
                } else {
                    ResponseData.Success(data = response as BaseTransactionResponse.LogonTransactionResponse)
                }
            } else {
                return ResponseData.Error(
                    error = getLoadSettingsException()
                )
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun init(
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
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        if (isNetworkAvailable()) {
            val transaction = InitTransaction(
                InitTransactionRequest(
                    terminalId = terminalId,
                    stan = generateStan(),
                    serial = serial,
                    appVersion = appVersion,
                    nii = nii,
                    date = getDateOfTransaction(),
                    time = getTimeOfTransaction(),
                    dateTimeInGMT = generateDateTimeInGMT(),
                    terminalLanguage = terminalLanguage,
                    acquiringInstitutionIdentificationCode = acquiringInstitutionIdentificationCode,
                    merchantId = merchantId,
                    posConditionCode = posConditionCode,
                    terminalConnectionType = terminalConnectionType
                ), macGenerator,
                connection
            )
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toInitTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            }
            else {
                ResponseData.Success(data = response as BaseTransactionResponse.InitTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun balance(
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
    ): ResponseData<BaseTransactionResponse.BalanceTransactionResponse> {
        if (isNetworkAvailable()) {
            val transaction = BalanceTransaction(
                BalanceTransactionRequest(
                    pan = pan,
                    terminalId = terminalId,
                    terminalConnectionType = terminalConnectionType,
                    terminalType = terminalType,
                    terminalLanguage = terminalLanguage,
                    track2 = track2,
                    pinBlock = pinBlock,
                    merchantId = merchantId,
                    POS = POS,
                    currency = currency,
                    stan = generateStan(),
                    serial = serial,
                    appVersion = appVersion,
                    nii = nii,
                    date = getDateOfTransaction(),
                    time = getTimeOfTransaction(),
                    posConditionCode = posConditionCode
                ), macGenerator, connection
            )
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) ResponseData.Error(
                data = response.toBalanceTransactionResponse(
                    getResponseMessageFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                ),
                error = getExceptionFromResponseCode(
                    response.responseCode, response.responseMessage
                )
            )
            else {
                ResponseData.Success(data = response as BaseTransactionResponse.BalanceTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun purchase(
        isNetworkAvailable: () -> Boolean,
        amount: String,
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
        purchaseId: String?,
        posConditionCode: String,
        apportionments: List<Apportionment>?
    ): ResponseData<BaseTransactionResponse.PurchaseTransactionResponse> {
        if (isNetworkAvailable()) {
            val date = getDateOfTransaction()
            val time = getTimeOfTransaction()
            val stan = generateStan()
            val transaction = PurchaseTransaction(PurchaseTransactionRequest(
                pan = pan,
                terminalId = terminalId,
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency,
                stan = stan,
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                amount = amount,
                date = date,
                time = time,
                acquiringInstitutionIdentificationCode = "",
                dateTimeInGMT = generateDateTimeInGMT(),
                posConditionCode = posConditionCode,
                purchaseId = purchaseId,
                apportionments = apportionments
            ),
                macGenerator,
                connection,
                saveTransactionLog = {
                    withContext(Dispatchers.IO){
                        val merchant = merchantLocalDataSource.getMerchant()
                        transactionLogDao.insert(
                            TransactionLogEntity(
                                id = 0,
                                processingCode = it.processCode,
                                amount = amount.toEnglishNumber().toLong(),
                                stan = stan.toString(),
                                dateTransaction = date,
                                timeTransaction = time,
                                terminalId = terminalId,
                                maskedPan = pan,
                                type = if (purchaseId.isNullOrEmpty()) TransactionType.PURCHASE.tag else TransactionType.PURCHASEWITHID.tag,
                                rrn = it.rrn,
                                issuer = it.cardIssuer,
                                responseCode = it.respCode,
                                responseMsg = null,
                                merchantPhone = merchant.merchantPhone,
                                merchantId = merchant.merchantId ?: "",
                                merchantName = merchant.merchantName ?: "",
                                timestamp = System.currentTimeMillis() / 1000
                            )
                        )
                    }
                },
                saveReverseData = {
                    val transactionForInQueue = TransactionQueueEntity(
                        dateTime = "${date}-${time}",
                        date = date,
                        time = time,
                        status = 'R',
                        printed = false,
                        processingCode = it.processCode,
                        amount = amount,
                        merchantId = merchantId,
                        stan = stan.toString(),
                        rrn = "",
                        maskedPan = pan,
                        responseCode = it.respCode,
                        type = if (purchaseId.isNullOrEmpty()) TransactionType.PURCHASE.tag else TransactionType.PURCHASEWITHID.tag,
                        issuer = "",
                        responseMsg = "",
                        posConditionCode = posConditionCode,
                        currency = currency,
                        terminalId = terminalId
                    )
                    transactionQueueDao.insert(
                        transactionForInQueue
                    )
                },
                sendTransactionInQueue = {
                    sendTransactionInQueue(
                        terminalLanguage = terminalLanguage,
                        terminalConnectionType = terminalConnectionType,
                        terminalType = terminalType
                    )
                },
                setStatusToSettle = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.updateStatusByDateTime(
                        'S', dateOfTransaction, timeOfTransaction
                    )
                },
                setStatusToReverse = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.updateStatusByDateTime(
                        'R', dateOfTransaction, timeOfTransaction
                    )
                },
                updateTransaction = { dateOfTransaction, timeOfTransaction, maskedPan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                    updateTransaction(
                        date = dateOfTransaction,
                        time = timeOfTransaction,
                        pan = pan,
                        cardIssuer = cardIssuer,
                        responseCode = responseCode,
                        amount = amount,
                        rrn = rrn,
                        serviceDesc = "",
                        pinVoucher = "",
                        serialVoucher = "",
                        mobileNumber = "",
                        operatorCode = -1,
                        responseMessage = responseMessage, trace = trace
                    )
                },
                deleteTransaction = { dateOfTransaction, timeOfTransaction ->
                    deleteTransaction(
                        dateOfTransaction,
                        timeOfTransaction
                    )
                },
                clearTransactionFromQueue = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.deleteByDateTime(dateOfTransaction, timeOfTransaction)
                })
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toPurchaseTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            } else {
                ResponseData.Success(data = response as BaseTransactionResponse.PurchaseTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun voucher(
        isNetworkAvailable: () -> Boolean,
        amount: String,
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
        productCode: String,
        posConditionCode: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse> {
        if (isNetworkAvailable()) {
            val date = getDateOfTransaction()
            val time = getTimeOfTransaction()
            val stan = generateStan()
            val transaction = VoucherTransaction(VoucherTransactionRequest(
                pan = pan,
                terminalId = terminalId,
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency,
                stan = stan,
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                amount = amount,
                date = date,
                time = time,
                productCode = productCode,
                posConditionCode = posConditionCode
            ),
                macGenerator,
                connection,
                saveTransactionLog = {
                    withContext(Dispatchers.IO) {
                        val merchant = merchantLocalDataSource.getMerchant()
                        transactionLogDao.insert(
                            TransactionLogEntity(
                                id = 0,
                                processingCode = it.processCode,
                                amount = amount.toEnglishNumber().toLong(),
                                stan = stan.toString(),
                                dateTransaction = date,
                                timeTransaction = time,
                                terminalId = terminalId,
                                maskedPan = pan,
                                type = TransactionType.VOUCHER.tag,
                                rrn = null,
                                issuer = null,
                                responseCode = it.respCode,
                                responseMsg = null,
                                merchantPhone = merchant.merchantPhone,
                                merchantId = merchant.merchantId ?: "",
                                merchantName = merchant.merchantName ?: "",
                                timestamp = System.currentTimeMillis() / 1000
                            )
                        )
                    }
                },
                saveReverseData = {
                    transactionQueueDao.insert(
                        TransactionQueueEntity(
                            dateTime = "${date}-${time}",
                            date = date,
                            time = time,
                            status = 'R',
                            printed = false,
                            processingCode = it.processCode,
                            amount = amount,
                            merchantId = merchantId,
                            stan = stan.toString(),
                            rrn = "",
                            maskedPan = pan,
                            responseCode = it.respCode,
                            type = TransactionType.VOUCHER.tag,
                            issuer = "",
                            responseMsg = "",
                            terminalId = terminalId,
                            currency = currency,
                            posConditionCode = posConditionCode
                        )
                    )
                },
                requestDecryptData = {
                    requestDecryptData(it)
                },
                sendTransactionInQueue = {
                    sendTransactionInQueue(
                        terminalLanguage,
                        terminalConnectionType = terminalConnectionType,
                        terminalType = terminalType
                    )
                },
                updateTransaction = { dateOfTransaction, timeOfTransaction, maskedPan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                    updateTransaction(
                        dateOfTransaction,
                        timeOfTransaction,
                        maskedPan,
                        cardIssuer,
                        responseCode,
                        amount,
                        rrn,
                        "",
                        pinVoucher,
                        serialVoucher,
                        "",
                        operatorCode,
                        responseMessage = responseMessage, trace = trace
                    )
                },
                deleteTransaction = { dateOfTransaction, timeOfTransaction ->
                    deleteTransaction(
                        dateOfTransaction,
                        timeOfTransaction
                    )
                },
                setStatusToSettle = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.updateStatusByDateTime(
                        'S', dateOfTransaction, timeOfTransaction
                    )
                },
                clearTransactionFromQueue = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.deleteByDateTime(dateOfTransaction, timeOfTransaction)
                })
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toVoucherTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            } else {
                ResponseData.Success(data = response as BaseTransactionResponse.VoucherTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun topUp(
        isNetworkAvailable: () -> Boolean,
        amount: String,
        mobile: String,
        productCode: String,
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
        posConditionCode: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.TopUpTransactionResponse> {
        if (isNetworkAvailable()) {
            val date = getDateOfTransaction()
            val time = getTimeOfTransaction()
            val stan = generateStan()
            val transaction = TopUpTransaction(TopUpTransactionRequest(
                pan = pan,
                terminalId = terminalId,
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency,
                stan = stan,
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                amount = amount,
                date = date,
                time = time,
                productCode = productCode,
                mobile = mobile,
                posConditionCode = posConditionCode
            ),
                macGenerator,
                connection,
                saveTransactionLog = {
                    withContext(Dispatchers.IO) {
                        val merchant = merchantLocalDataSource.getMerchant()
                        transactionLogDao.insert(
                            TransactionLogEntity(
                                id = 0,
                                processingCode = it.processCode,
                                amount = amount.toEnglishNumber().toLong(),
                                stan = stan.toString(),
                                dateTransaction = date,
                                timeTransaction = time,
                                terminalId = terminalId,
                                maskedPan = pan,
                                type = TransactionType.TOPUP.tag,
                                rrn = null,
                                issuer = null,
                                responseCode = it.respCode,
                                responseMsg = null,
                                merchantName = merchant.merchantName ?: "",
                                merchantPhone = merchant.merchantPhone,
                                merchantId = merchant.merchantId ?: "",
                                timestamp = System.currentTimeMillis() / 1000
                            )
                        )
                    }
                },
                saveReverseData = {
                    transactionQueueDao.insert(
                        TransactionQueueEntity(
                            dateTime = "${date}-${time}",
                            date = date,
                            time = time,
                            status = 'R',
                            printed = false,
                            processingCode = it.processCode,
                            amount = amount,
                            merchantId = merchantId,
                            stan = stan.toString(),
                            rrn = "",
                            maskedPan = pan,
                            responseCode = it.respCode,
                            type = TransactionType.TOPUP.tag,
                            issuer = "",
                            responseMsg = "",
                            currency = currency,
                            terminalId = terminalId,
                            posConditionCode = posConditionCode
                        )
                    )
                },
                sendTransactionInQueue = {
                    sendTransactionInQueue(
                        terminalLanguage,
                        terminalConnectionType = terminalConnectionType,
                        terminalType = terminalType
                    )
                },
                updateTransaction = { dateOfTransaction, timeOfTransaction, maskedPan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                    updateTransaction(
                        dateOfTransaction,
                        timeOfTransaction,
                        maskedPan,
                        cardIssuer,
                        responseCode,
                        amount,
                        rrn,
                        "",
                        "",
                        "",
                        mobileNumber,
                        operatorCode,
                        responseMessage, trace
                    )
                },
                deleteTransaction = { dateOfTransaction, timeOfTransaction ->
                    deleteTransaction(
                        dateOfTransaction,
                        timeOfTransaction
                    )
                },
                setStatusToSettle = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.updateStatusByDateTime(
                        'S', dateOfTransaction, timeOfTransaction
                    )
                },
                clearTransactionFromQueue = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.deleteByDateTime(dateOfTransaction, timeOfTransaction)
                })
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toTopupTransactionResponse(
                        responseMessage = getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            } else {
                ResponseData.Success(data = response as BaseTransactionResponse.TopUpTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    private fun updateTransaction(
        date: String,
        time: String,
        pan: String,
        cardIssuer: String,
        responseCode: String,
        amount: String,
        rrn: String?,
        serviceDesc: String?,
        pinVoucher: String?,
        serialVoucher: String?,
        mobileNumber: String?,
        operatorCode: Int?,
        responseMessage: String?,
        trace: String?
    ) {
        with(transactionLogDao) {
            val transaction = getByDateTime(date, time)
            transaction?.rrn = rrn
            transaction?.issuer = cardIssuer
            transaction?.responseCode = responseCode.toInt()
            transaction?.maskedPan = pan
            transaction?.responseMsg = responseMessage
            if (cardIssuer != null) transaction?.issuer = cardIssuer
            if (serviceDesc != null) transaction?.serviceDesc = serviceDesc
            if (pinVoucher != null) transaction?.pinVoucher = pinVoucher
            if (serialVoucher != null) transaction?.serialVoucher = serialVoucher
            if (mobileNumber != null) transaction?.mobileNumber = mobileNumber
            if (operatorCode != null) transaction?.operatorCode = operatorCode
            if (trace != null){
                transaction?.stan = trace.trim()
            }
            if (transaction != null)
                update(transaction)
        }

    }

    private fun deleteTransaction(
        date: String,
        time: String
    ) {
        transactionLogDao.deleteByDateTime(date, time)

    }

    override suspend fun billInquiry(
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
    ): ResponseData<BaseTransactionResponse.BillInquiryTransactionResponse> {
        if (isNetworkAvailable()) {
            val stan = generateStan()
            val transaction = BillInquiryTransaction(BillInquiryTransactionRequest(
                terminalId = terminalId,
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                merchantId = merchantId,
                stan = stan,
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                date = getDateOfTransaction(),
                time = getTimeOfTransaction(),
                billId = billID,
                payId = payId,
                posConditionCode = posConditionCode
            ), macGenerator, connection, sendTransactionInQueue = {
                sendTransactionInQueue(
                    terminalLanguage,
                    terminalConnectionType = terminalConnectionType,
                    terminalType = terminalType
                )
            })
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toBillInquiryTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            } else {
                ResponseData.Success(data=response as BaseTransactionResponse.BillInquiryTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    override suspend fun billPay(
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
    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse> {
        if (isNetworkAvailable()) {
            val date = getDateOfTransaction()
            val time = getTimeOfTransaction()
            val stan = generateStan()
            val transaction = BillPayTransaction(BillPayTransactionRequest(
                amount = amount,
                terminalId = terminalId,
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                merchantId = merchantId,
                stan = stan,
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                date = date,
                time = time,
                billId = billID,
                payId = payId,
                pan = pan,
                track2 = track2,
                pinBlock = pinBlock,
                POS = POS,
                currency = currency,
                serviceDesc = serviceDesc ?: "",
                posConditionCode = posConditionCode
            ),// TODO:
                macGenerator,
                connection,
                saveTransactionLog = {
                    withContext(Dispatchers.IO) {
                        val merchant = merchantLocalDataSource.getMerchant()
                        transactionLogDao.insert(
                            TransactionLogEntity(
                                id = 0,
                                processingCode = it.processCode,
                                amount = amount.toEnglishNumber().toLong(),
                                stan = stan.toString(),
                                dateTransaction = date,
                                timeTransaction = time,
                                terminalId = terminalId,
                                maskedPan = pan,
                                type = TransactionType.BILL_PAY.tag,
                                rrn = null,
                                issuer = null,
                                responseCode = it.respCode,
                                responseMsg = null,
                                merchantName = merchant.merchantName ?: "",
                                merchantPhone = merchant.merchantPhone,
                                merchantId = merchant.merchantId ?: "",
                                timestamp = System.currentTimeMillis() / 1000
                            )
                        )
                    }
                },
                saveReverseData = {
                    transactionQueueDao.insert(
                        TransactionQueueEntity(
                            dateTime = "${date}-${time}",
                            date = date,
                            time = time,
                            status = 'R',
                            printed = false,
                            processingCode = it.processCode,
                            amount = amount,
                            merchantId = merchantId,
                            stan = stan.toString(),
                            rrn = "",
                            maskedPan = pan,
                            responseCode = it.respCode,
                            type = TransactionType.BILL_PAY.tag,
                            issuer = "",
                            responseMsg = "",
                            posConditionCode = posConditionCode,
                            terminalId = terminalId,
                            currency = currency
                        )
                    )
                },
                sendTransactionInQueue = {
                    sendTransactionInQueue(
                        terminalLanguage,
                        terminalConnectionType = terminalConnectionType,
                        terminalType = terminalType
                    )
                },
                updateTransaction = { dateOfTransaction, timeOfTransaction, maskedPan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                    updateTransaction(
                        dateOfTransaction,
                        timeOfTransaction,
                        maskedPan,
                        cardIssuer,
                        responseCode,
                        amount,
                        rrn,
                        serviceDesc,
                        "",
                        "",
                        "",
                        -1,
                        responseMessage = responseMessage, trace = trace
                    )

                },
                deleteTransaction = { dateOfTransaction, timeOfTransaction ->
                    deleteTransaction(
                        dateOfTransaction,
                        timeOfTransaction
                    )
                },
                setStatusToSettle = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.updateStatusByDateTime(
                        'S', dateOfTransaction, timeOfTransaction
                    )

                },
                clearTransactionFromQueue = { dateOfTransaction, timeOfTransaction ->
                    transactionQueueDao.deleteByDateTime(dateOfTransaction, timeOfTransaction)
                })
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toBillPayTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            } else {
                ResponseData.Success(data = response as BaseTransactionResponse.BillPayTransactionResponse)
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }

    private fun generateStan(): Int {
        return stanGenerator.generate()
    }


    private suspend fun sendTransactionInQueue(
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String
    ): Boolean {
        var transactionsInQueue = transactionQueueDao.getAll()
        if (!transactionsInQueue.isNullOrEmpty()) {
            transactionsInQueue.forEach {
                val settlementReverseTransactionRequest = SettlementReverseTransactionRequest(
                    stan = it.stan.toInt(),
                    processingCode = it.processingCode,
                    status = it.status,
                    amount = it.amount,
                    currency = it.currency,
                    terminalConnectionType = terminalConnectionType,
                    terminalType = terminalType,
                    merchantId = it.merchantId,
                    date = it.date,
                    time = it.time,
                    posConditionCode = it.posConditionCode,
                    nii = connectionSettingsDataSource.getNii(),
                    appVersion = BuildConfig.VERSION_NAME,
                    serial = "",
                    terminalId = it.terminalId,
                    terminalLanguage = terminalLanguage
                )
                val transaction = SettlementReverseTransaction(settlementReverseTransactionRequest,
                    macGenerator,
                    connection,
                    saveReverseData = {},
                    updateTransaction = { date, time, pan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                    })
                val response = transaction.execute()
                if (response.responseCode != 80) {
                    transactionQueueDao.deleteByDateTime(it.date, it.time)
                }

            }
        }
        transactionsInQueue = transactionQueueDao.getAll()
        return transactionsInQueue.isNullOrEmpty()

    }

    override suspend fun settlementReverse(
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String
    ) {
        sendTransactionInQueue(
            terminalLanguage,
            terminalConnectionType = terminalConnectionType,
            terminalType = terminalType
        )
    }

    override suspend fun getTransactionInQueue(
        dateOfTransaction: String, timeOfTransaction: String
    ): TransactionInQueue? {
        return transactionQueueDao.getByDateTime(dateOfTransaction, timeOfTransaction)
            ?.toTransactionInQueue()
    }

    override suspend fun updatePrintStatusOfTransactionInQueue(transactionInQueue: TransactionInQueue?) {
        if (transactionInQueue != null) transactionQueueDao.updatePrintStatus(
            printed = true, date = transactionInQueue.date, time = transactionInQueue.time
        )
    }

    override suspend fun updatePrintStatusOfTransactionInQueue(printStatus: Boolean) {
        val transactionQueues = transactionQueueDao.getAll()
        if (!transactionQueues.isNullOrEmpty()) transactionQueueDao.updatePrintStatus(
            printed = printStatus,
            date = transactionQueues[0].date,
            time = transactionQueues[0].time
        )
    }

    override suspend fun getLastTxnIsNotPrinted(): TransactionLog? {
        val temp = transactionQueueDao.getLastTxnByPrinted(false)
        if (temp != null) {
            val lastTransaction =
                transactionLogDao.getByDateTime(date_ = temp.date, time = temp.time)
            return lastTransaction?.toTransaction()
        } else return null
    }

    private fun getResponseMessageFromResponseCode(
        responseCode: Int,
        responseMessage: String?
    ): String {
     return  if (responseMessage.isNullOrEmpty()) ResponseMessageContainer.valueOfLabel(
            responseCode.toString(),
        ).message else responseMessage
    }

    private fun getExceptionFromResponseCode(
        responseCode: Int,
        responseMessage: String?
    ): TotanPayException {
        return if (responseMessage.isNullOrEmpty()) {
            TotanPayException(
                messageError = ResponseMessageContainer.valueOfLabel(
                    responseCode.toString(),
                ).message, TotanPayException.Type.NORMAL
            )
        } else {
            TotanPayException(messageError = responseMessage, TotanPayException.Type.NORMAL)
        }
    }

    private fun getLoadSettingsException(
    ): TotanPayException {
        return TotanPayException(
            messageError = "تنظیمات ارتباطی وجود ندارد لطفا به قسمت تنظیمات اتصال بروید و  تنظیمات را تعیین نمایید",
            TotanPayException.Type.LOAD_SETTINGS
        )

    }

}



