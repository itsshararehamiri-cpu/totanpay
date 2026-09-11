package com.example.totanpay.data.repository.settings.report
import com.example.totanpay.data.repository.datasource.report.ReportLocalDataSource
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.toResponseTransaction
import com.example.totanpay.data.util.formatTime
import com.example.totanpay.data.util.getPersianDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject


class ReportRepositoryImpl @Inject constructor(
    private val dataSource: ReportLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ReportRepository {
    override suspend fun getLastTransaction(): ResponseTransaction? {
        return withContext(ioDispatcher) {
            val lastTransaction = dataSource.getLastTransaction()?.toResponseTransaction()
            val time: String
            val date: String
            if (lastTransaction != null) {
                time = lastTransaction.time.formatTime()
                date = getPersianDate(lastTransaction.date)
                lastTransaction.time = time
                lastTransaction.date = date
                lastTransaction.transactionType =
                    TransactionType.titleOf(lastTransaction.transactionType.toInt()).title
            }
            lastTransaction
        }
    }

    override suspend fun geTransactionBasedStan(stan: String,traceIsSelected: Boolean): ResponseTransaction? {
        return withContext(ioDispatcher) {
            val searchedTransaction =
                dataSource.geTransactionBasedStan(stan,traceIsSelected)?.toResponseTransaction()
            val time: String
            val date: String
            if (searchedTransaction != null) {
                time = searchedTransaction.time.formatTime()
                date = getPersianDate(searchedTransaction.date)
                searchedTransaction.time = time
                searchedTransaction.date = date
                searchedTransaction.transactionType =
                    TransactionType.titleOf(searchedTransaction.transactionType.toInt()).title
            }
            searchedTransaction
        }

    }

    override suspend fun getDetailsTransaction(
        fromDate: PersianDate?,
        toDate: PersianDate?,
        fromAmount: String?,
        toAmount: String?,
        selectedTransactions: String?
    ): List<ResponseTransaction>? {
        return withContext(ioDispatcher) {
            val jsonObject = selectedTransactions?.takeIf { it.isNotEmpty() }?.let { JSONObject(it) }
            val purchaseIsSelected = jsonObject?.optString("purchaseType") != "dontHas"
            val billPayIsSelected = jsonObject?.optString("billPayType") != "dontHas"
            val voucherIsSelected = jsonObject?.optString("voucherType") != "dontHas"
            val topUpIsSelected = jsonObject?.optString("topupType") != "dontHas"

            val fromAmountValue = fromAmount.takeUnless { it.isNullOrEmpty() || it == "-1" } ?: "0"
            val toAmountValue = toAmount.takeUnless { it.isNullOrEmpty() || it == "-1" }
                ?: dataSource.getMaximumAmountOfTransactions().toString()

            val fromDateSeconds = fromDate?.let { it.time / 1000 } ?: 0L
            val toDateSeconds = toDate?.let { it.time / 1000 } ?: (System.currentTimeMillis() / 1000)

            val detailsTransactions = dataSource.getDetailsTransaction(
                fromDate = minOf(fromDateSeconds, toDateSeconds),
                toDate = maxOf(fromDateSeconds, toDateSeconds),
                fromAmount = fromAmountValue,
                toAmount = toAmountValue,
                purchaseIsSelected = purchaseIsSelected,
                voucherIsSelected = voucherIsSelected,
                topupIsSelected = topUpIsSelected,
                billPayIsSelected = billPayIsSelected
            )
            detailsTransactions?.map {
                it.toResponseTransaction()
            }?.map {
                val time1 = it?.time.formatTime()
                val date = getPersianDate(it!!.date)
                it!!.copy(
                    date = date,
                    time = time1,
                    transactionType = TransactionType.titleOf(it.transactionType.toInt()).title
                )
            }
        }

    }
}