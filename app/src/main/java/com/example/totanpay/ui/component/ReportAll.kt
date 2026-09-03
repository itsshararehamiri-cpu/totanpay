package com.example.totanpay.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddSumOfAllTransactions
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.ui.component.report.DateHeader
import com.example.totanpay.ui.component.report.HeaderRow
import com.example.totanpay.ui.component.report.TransactionRow
import com.example.totanpay.ui.theme.Black

@Composable
fun ReportAll(
    results: List<ResponseTransaction>,
    sumOfTransactions: String
) {
    val isPaperReceipt = true
    val firstColor = Black
    Column(
        Modifier.fillMaxWidth(  if (isPaperReceipt) {
            if (isSmall(LocalContext.current)) {
                1f
            } else 0.5f
        } else 1f)
            .fillMaxHeight()
            .background(White)
    ) {
        HeaderRow(
            modifier = Modifier.fillMaxWidth(1f), listOf(
                stringResource(id = R.string.transaction_type),
                stringResource(id = R.string.amount),
                stringResource(id = R.string.trace),
                stringResource(id = R.string.time),
                stringResource(id = R.string.row1)
            ), textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .layoutId("mainContentReceipt")
                .padding(horizontal = if (isPaperReceipt) 0.dp else 12.dp)
                .border(
                    1.dp, MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                        topStart = 0.dp,
                        topEnd = 0.dp
                    )
                )
                .background(color = if (isPaperReceipt) White else MaterialTheme.colorScheme.surface)
        ) {
            Column {
                val groupedTransactions = results.groupBy { it.date }
                Column(Modifier) {
                    groupedTransactions.forEach { (dateTransaction, transactions) ->
                        DateHeader(dateTransaction = dateTransaction, isPaperReceipt)
                        Column {
                            transactions.forEachIndexed { index, transaction ->
                                TransactionRow(
                                    transaction = transaction,
                                    index + 1,
                                    textColor = firstColor,
                                    isPaperReceipt
                                )
                            }
                        }
                    }
                }
            }
        }
        if(isPaperReceipt)
        AddSumOfAllTransactions(
            Modifier,
            sum = sumOfTransactions,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        if (isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = isPaperReceipt
            )
        }
    }
}