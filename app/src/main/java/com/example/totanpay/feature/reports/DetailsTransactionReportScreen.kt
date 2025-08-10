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