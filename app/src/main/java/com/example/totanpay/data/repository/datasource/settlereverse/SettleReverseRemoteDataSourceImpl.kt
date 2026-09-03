package com.example.totanpay.data.repository.datasource.settlereverse

import android.util.Log
import androidx.multidex.BuildConfig
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.toTransaction
import com.example.totanpay.data.entity.toTransactionInQueue
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.SettlementReverseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionInQueue
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.SettlementReverseTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettleReverseRemoteDataSourceImpl @Inject constructor(
    private val macGenerator: IMacGenerator,
    private val connection: IConnection,
    private val transactionLogDao: TransactionLogDao,
    private val transactionQueueDao: TransactionQueueDao,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource
) : SettleReverseRemoteDataSource {
    private val queueMutex = Mutex()

    init {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()

            )
        }
    }

    override suspend fun sendTransactionInQueue(
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String
    ): Boolean {
        sendTransactionInQueue2(terminalLanguage, terminalConnectionType, terminalType)
        val ret = transactionQueueDao.getAll().isNullOrEmpty()
        if (!ret) {
            GlobalScope.launch(Dispatchers.IO)
            {
                delay(120000)
                sendTransactionInQueue(terminalLanguage, terminalConnectionType, terminalType)
            }
        }
        return ret
    }

    suspend fun sendTransactionInQueue2(
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String
    ): Boolean {
        queueMutex.withLock {

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
                    val transaction = SettlementReverseTransaction(
                        settlementReverseTransactionRequest,
                        macGenerator,
                        connection,
                        saveReverseData = {},
                        updateTransaction = { date, time, pan, cardIssuer, responseCode, amount, rrn, serviceDesc, pinVoucher, serialVoucher, mobileNumber, operatorCode, responseMessage, trace ->
                        })
                    val response = transaction.execute()
                    if (response.responseCode != 80 && response.responseCode >= 0) {
                        transactionQueueDao.deleteByDateTime(it.date, it.time)
                    }

                }
            }
            transactionsInQueue = transactionQueueDao.getAll()
            return transactionsInQueue.isNullOrEmpty()
        }

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
        if (transactionInQueue != null) {
            transactionQueueDao.updatePrintStatus(
                printed = true, date = transactionInQueue.date, time = transactionInQueue.time
            )
        }
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

    override suspend fun hasTransactionInQueue(): Boolean {
        val transactionQueues = transactionQueueDao.getAll()
        return !transactionQueues.isNullOrEmpty()

    }
}