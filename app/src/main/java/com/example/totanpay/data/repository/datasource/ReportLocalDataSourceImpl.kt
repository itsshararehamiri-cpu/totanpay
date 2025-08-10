package com.example.totanpay.data.repository.datasource

import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.entity.toTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.toEnglishNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ReportLocalDataSourceImpl @Inject constructor(
    private val transactionLogDao: TransactionLogDao

) : ReportLocalDataSource {
    override suspend fun getLastTransaction(): TransactionLog? {
        val lastTransaction =
            transactionLogDao.getLast(
                exceptType = TransactionType.BALANCE.tag,
                responseCode = 0
            )
        return lastTransaction?.toTransaction()
    }

    override suspend fun geTransactionBasedStan(stan: String): TransactionLog? {
        val transaction = transactionLogDao.getByStan(stan = stan)
        if (transaction != null) {
            val searchedTransaction = transaction?.toTransaction()
            return searchedTransaction
        } else return null
    }

    override suspend fun a() {
        withContext(Dispatchers.IO) {
            delay(1000)
//            for (i in 1..45) {
//                transactionLogDao.insert(
//                    TransactionLogEntity(
//                        id = 0,
//                        processingCode = "000000",
//                        amount = "10000".toLong(),
//                        stan = i.toString(),
//                        dateTransaction = getDateOfTransaction(),
//                        timeTransaction = getTimeOfTransaction(),
//                        terminalId = "100000",
//                        maskedPan = "6037*********",
//                        type = if (i % 4 == 0) TransactionType.PURCHASE.tag
//                        else if (i % 3 == 0) TransactionType.BILL_PAY.tag
//                        else if (i % 5 == 0) TransactionType.VOUCHER.tag else TransactionType.TOPUP.tag,
//                        rrn = (i * 10 + 3 + (i % 2) + (i / 7)).toString(),
//                        issuer = "صادرت",
//                        responseCode = 0,
//                        responseMsg = null,
//                        merchantPhone = "0217894562", merchantId = "5555555555",
//                        timestamp = System.currentTimeMillis() / 1000
//                    )
//                )
//            }
        }
    }

    override suspend fun getDetailsTransaction(
        fromDate: String,
        toDate: String,
        fromAmount: String,
        toAmount: String,
        purchaseIsSelected: Boolean,
        billPayIsSelected: Boolean,
        voucherIsSelected: Boolean,
        topupIsSelected: Boolean
    ): List<TransactionLog>? {
        var transactionTypes: MutableList<Int> = mutableListOf()
        if (purchaseIsSelected)
            transactionTypes.add(TransactionType.PURCHASE.tag)
        if (voucherIsSelected)
            transactionTypes.add(TransactionType.VOUCHER.tag)
        if (billPayIsSelected)
            transactionTypes.add(TransactionType.BILL_PAY.tag)
        if (topupIsSelected)
            transactionTypes.add(TransactionType.TOPUP.tag)
        return transactionLogDao.getInRangeDate(
            fromDate,
            toDate,
            fromAmount.toEnglishNumber().toLong(),
            toAmount.toEnglishNumber().toLong(),
            transactionTypes
        )?.map {
            it?.toTransaction()!! // TODO:
        }
    }

    override suspend fun getDetailsTransaction(
        fromDate: Long,
        toDate: Long, fromAmount: String,
        toAmount: String,
        purchaseIsSelected: Boolean,
        billPayIsSelected: Boolean,
        voucherIsSelected: Boolean,
        topupIsSelected: Boolean
    ): List<TransactionLog>? {
        var transactionTypes: MutableList<Int> = mutableListOf<Int>()
        if (purchaseIsSelected) {
            transactionTypes.add(TransactionType.PURCHASE.tag)
            transactionTypes.add(TransactionType.PURCHASEWITHID.tag)
        }
        if (voucherIsSelected)
            transactionTypes.add(TransactionType.VOUCHER.tag)
        if (billPayIsSelected)
            transactionTypes.add(TransactionType.BILL_PAY.tag)
        if (topupIsSelected)
            transactionTypes.add(TransactionType.TOPUP.tag)
        return transactionLogDao.getInRangeDate(
            fromDate,
            toDate,
            fromAmount = fromAmount.toLong(),
            toAmount = toAmount.toLong(),
            transactionTypes as List<Int>
        )?.map {
            it?.toTransaction()!!
        }
    }

    override suspend fun getMaximumAmountOfTransactions(): Long {
        return transactionLogDao.getMaxAmount() ?: 0L
    }

}