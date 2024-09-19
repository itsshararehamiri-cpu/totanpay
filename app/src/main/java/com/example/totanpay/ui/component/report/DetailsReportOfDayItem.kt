package com.example.totanpay.ui.component.report
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

@Composable
fun DetailsReportOfDayItem(
    modifier: Modifier, reports: List<ResponseTransaction>, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    Box(
        modifier = modifier
            .layoutId("mainContentReceipt")
            .padding(horizontal = 12.dp)
            .border(
                1.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(
                    bottomStart = 16.dp, bottomEnd = 16.dp, topStart = 0.dp, topEnd = 0.dp
                )
            )
            .background(
                color = if (isPaperReceipt) White else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(
                    bottomStart = 16.dp, bottomEnd = 16.dp, topStart = 0.dp, topEnd = 0.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 16.dp, bottomEnd = 16.dp, topStart = 0.dp, topEnd = 0.dp
                )
            )
    )
    {
        Column(Modifier.padding(bottom = 5.dp)) {
            val groupedTransactions = reports.groupBy { it.date }
            LazyColumn(Modifier) {
                groupedTransactions.forEach { (dateTransaction, transactions) ->
                    item {
                        DateHeader(dateTransaction = dateTransaction, isPaperReceipt)
                    }
                    itemsIndexed(transactions) { index, transaction ->
                        TransactionRow(
                            transaction = transaction,
                            index + 1,
                            textColor = textColor,
                            isPaperReceipt
                        )
                    }
                }
            }

        }
    }
}
