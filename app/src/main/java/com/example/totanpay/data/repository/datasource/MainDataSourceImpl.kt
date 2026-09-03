package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import javax.inject.Inject


class MainDataSourceImpl @Inject constructor(
    private val macGenerator: IMacGenerator,
    private val connection: IConnection,
    private val transactionLogDao: TransactionLogDao,
    private val stanGenerator: StanGenerator,
    private val transactionQueueDao: TransactionQueueDao,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val logLocalDataSource: LogLocalDataSource
) : MainDataSource {
    init {
        loadSettings()
    }

    override fun loadSettings() {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()
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

    private fun deleteTransaction(
        date: String,
        time: String
    ) {
        transactionLogDao.deleteByDateTime(date, time)

    }
    private fun generateStan(): Int {
        return stanGenerator.generate()
    }


}



