package com.example.totanpay.feature.reports
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.report.ReportItem
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE
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
                val toolBar = createRefFor("toolBar")
                val lastTransaction = createRefFor("lastTransaction")
                val detailsOfTransactionsId = createRefFor("detailsOfTransactions")
                val reportBasedOnTrace = createRefFor("reportBasedOnTrace")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(lastTransaction) {
                    top.linkTo(toolBar.bottom, 30.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(detailsOfTransactionsId) {
                    top.linkTo(lastTransaction.bottom)
                    end.linkTo(lastTransaction.end)
                    start.linkTo(lastTransaction.start)
                    width = Dimension.fillToConstraints
                }
                constrain(reportBasedOnTrace) {
                    top.linkTo(detailsOfTransactionsId.bottom)
                    end.linkTo(lastTransaction.end)
                    start.linkTo(lastTransaction.start)
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.reports), modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            ReportItem(
                modifier = Modifier
                    .layoutId("lastTransaction"),
                backgroundImageId = R.drawable.ic_item_settings1,
                title = stringResource(id = R.string.last_transaction)
            ) {
                onLastTransactionClicked()
            }
            ReportItem(
                modifier = Modifier
                    .layoutId("detailsOfTransactions"),
                backgroundImageId = R.drawable.ic_item_settings2,
                title = stringResource(id = R.string.details_of_transactions)
            ) {
                onDetailsOfTransactionsClicked()
            }
            ReportItem(
                modifier = Modifier
                    .layoutId("reportBasedOnTrace"),
                backgroundImageId = R.drawable.ic_item_settings_4,
                title = stringResource(id = R.string.report_based_on_trace)
            ) {
                onReportBasedOnTraceClicked()
            }
        }
            Image(
                painter = painterResource(id = R.drawable.totan),
                contentDescription = "",
                modifier = Modifier
                    .size(TOTAN_ICON_SIZE)
                    .align(Alignment.BottomCenter)
            )
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