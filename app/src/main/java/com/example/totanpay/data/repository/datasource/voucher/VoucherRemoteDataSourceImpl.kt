package com.example.totanpay.data.repository.datasource.voucher

import com.example.totanpay.data.ConnectionException
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.StanGenerator
import com.example.totanpay.data.repository.datasource.TotanPayException
import com.example.totanpay.data.repository.datasource.getDateOfTransaction
import com.example.totanpay.data.repository.datasource.getTimeOfTransaction
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.settlereverse.SettleReverseRemoteDataSource
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.VoucherTransaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.VoucherTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toVoucherTransactionResponse
import com.example.totanpay.data.repository.util.getExceptionFromResponseCode
import com.example.totanpay.data.repository.util.getResponseMessageFromResponseCode
import com.example.totanpay.data.util.toEnglishNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherRemoteDataSourceImpl @Inject constructor(private val macGenerator: IMacGenerator,
                                                      private val connection: IConnection,
                                                      private val transactionLogDao: TransactionLogDao,
                                                      private val stanGenerator: StanGenerator,
                                                      private val transactionQueueDao: TransactionQueueDao,
                                                      connectionSettingsDataSource: ConnectionSettingsDataSource,
                                                      private val merchantLocalDataSource: MerchantLocalDataSource,
                                                      private val settleReverseRemoteDataSource: SettleReverseRemoteDataSource,
                                                      private val logLocalDataSource: LogLocalDataSource): VoucherRemoteDataSource {
    init {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()

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
        logs: String?,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse> {
        if (isNetworkAvailable()) {
            val date = getDateOfTransaction()
            val time = getTimeOfTransaction()
            val stan =  stanGenerator.generate()
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
                posConditionCode = posConditionCode,
                logs =logs
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
                    settleReverseRemoteDataSource.sendTransactionInQueue(
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
                    transactionLogDao.deleteByDateTime(
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
        responseMessage: Int?,
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
}