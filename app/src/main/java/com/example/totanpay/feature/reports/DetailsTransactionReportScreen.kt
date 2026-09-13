package com.example.totanpay.feature.reports

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
@Composable
fun DetailsTransactionReportScreen(
    viewModel: DetailsTransactionViewModel,
    onBackClicked: () -> Unit,
    onShowNotFound: () -> Unit,
    onSearchTransaction: (String, String, String, String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.showNotFound) {
        if (uiState.showNotFound) {
            onShowNotFound()
        }
    }
    BackHandler {
        onBackClicked()
    }
    DetailTransactionReportContent(onBackButtonClicked = { onBackClicked() }) { fromDateValue, toDateValue,
                                                                                fromAmount, toAmount, selectedTransactions ->
        onSearchTransaction(
            fromDateValue,
            toDateValue,
            fromAmount.ifEmpty { "0" },
            toAmount.ifEmpty { "-1" },
            selectedTransactions
        )
    }
}
data class DateContainer(
    val year: String, val month: String, val day: String,
    val hour: String, val minute: String
)

fun DateContainer.toDisplayText(): String {
    val time = "${hour.ifEmpty { "0" }.padStart(2, '0')}:${minute.ifEmpty { "0" }.padStart(2, '0')}"
    return "$year/$month/$day $time"
}

fun DateContainer.plusOneMinute(): DateContainer {
    val totalMinutes = (hour.ifEmpty { "0" }.toIntOrNull() ?: 0) * 60 +
            (minute.ifEmpty { "0" }.toIntOrNull() ?: 0) + 1
    return copy(hour = ((totalMinutes / 60) % 24).toString(), minute = (totalMinutes % 60).toString())
}