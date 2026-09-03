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
            val jsonObject = JSONObject(selectedTransactions)
            val purchaseIsSelected = jsonObject.getString("purchaseType") == "has"
            val billPayIsSelected = jsonObject.getString("billPayType") == "has"
            val voucherIsSelected = jsonObject.getString("voucherType") == "has"
            val topUpIsSelected = jsonObject.getString("topupType") == "has"
            var toAmountTemp = "-1"
            if (toAmount == "-1") toAmountTemp =
                dataSource.getMaximumAmountOfTransactions().toString()
            else toAmountTemp =
                if (toAmount.isNullOrEmpty()) dataSource.getMaximumAmountOfTransactions()
                    .toString() else toAmount
            val detailsTransactions =
                if (fromDate != null && toDate != null && !fromAmount.isNullOrEmpty() && !toAmount.isNullOrEmpty() && !selectedTransactions.isNullOrEmpty()) dataSource.getDetailsTransaction(
                    fromDate = ((fromDate.time / 1000)..(toDate.time / 1000)).first,
                    toDate = ((fromDate.time / 1000)..(toDate.time / 1000)).last,
                    fromAmount = fromAmount,
                    toAmount = toAmountTemp,
                    purchaseIsSelected = purchaseIsSelected,
                    voucherIsSelected = voucherIsSelected,
                    topupIsSelected = topUpIsSelected,
                    billPayIsSelected = billPayIsSelected
                )
                else {
                    dataSource.getDetailsTransaction(
                        fromDate = ((fromDate!!.time / 1000)..(toDate!!.time / 1000)).first,
                        toDate = ((fromDate.time / 1000)..(toDate.time / 1000)).last,
                        fromAmount = fromAmount!!,
                        toAmount = toAmount!!,
                        purchaseIsSelected = purchaseIsSelected,
                        voucherIsSelected = voucherIsSelected,
                        topupIsSelected = topUpIsSelected,
                        billPayIsSelected = billPayIsSelected
                    )
                }
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