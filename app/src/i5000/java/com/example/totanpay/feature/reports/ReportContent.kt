package com.example.totanpay.feature.reports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.compound.I5000SettingsItem
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ReportContent(
    onBackClicked: () -> Unit, onLastTransactionClicked: () -> Unit,
    onDetailsOfTransactionsClicked: () -> Unit, onReportBasedOnTraceClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val lastTransaction = createRefFor("lastTransaction")
                val detailsOfTransactionsId = createRefFor("detailsOfTransactions")
                val reportBasedOnTrace = createRefFor("reportBasedOnTrace")
                constrain(lastTransaction) {
                    top.linkTo(parent.top,40.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(detailsOfTransactionsId) {
                    top.linkTo(lastTransaction.bottom,10.dp)
                    end.linkTo(lastTransaction.end)
                    start.linkTo(lastTransaction.start)
                    width = Dimension.fillToConstraints
                }
                constrain(reportBasedOnTrace) {
                    top.linkTo(detailsOfTransactionsId.bottom,10.dp)
                    end.linkTo(lastTransaction.end)
                    start.linkTo(lastTransaction.start)
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            I5000SettingsItem(
                modifier = Modifier
                    .layoutId("lastTransaction"),
                iconImageId = null,
                title = stringResource(id = R.string.last_transaction)
            ) {
                onLastTransactionClicked()
            }
            I5000SettingsItem(
                modifier = Modifier
                    .layoutId("detailsOfTransactions"),
                iconImageId =null,
                title = stringResource(id = R.string.details_of_transactions)
            ) {
                onDetailsOfTransactionsClicked()
            }
            I5000SettingsItem (
                modifier = Modifier
                    .layoutId("reportBasedOnTrace"),
                iconImageId = null,
                title = stringResource(id = R.string.report_based_on_trace)
            ) {
                onReportBasedOnTraceClicked()
            }
        }
    }
}

@Composable
@Preview
fun ReportContentPreview() {
    TotanPayTheme {
        ReportContent(
            onBackClicked = {},
            onLastTransactionClicked = {},
            onDetailsOfTransactionsClicked = {},
            onReportBasedOnTraceClicked = {})

    }
}