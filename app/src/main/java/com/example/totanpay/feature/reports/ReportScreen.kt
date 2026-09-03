package com.example.totanpay.feature.reports

import androidx.compose.runtime.Composable

@Composable
fun MenuReportScreen(
    onBackClicked: () -> Unit,
    onLastTransactionClicked: () -> Unit,
    onDetailsOfTransactionsClicked: () -> Unit,
    onReportBasedOnTraceClicked: () -> Unit
) {
    ReportContent(currentLanguageIsFarsi = true,
        onBackClicked = { onBackClicked() },
        onLastTransactionClicked = { onLastTransactionClicked() },
        onDetailsOfTransactionsClicked = { onDetailsOfTransactionsClicked() },
        onReportBasedOnTraceClicked = { onReportBasedOnTraceClicked() })
}

