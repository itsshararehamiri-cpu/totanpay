package com.example.totanpay.feature.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.SquareRoundedButton
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100

@Composable
fun MeunReportScreen(
    onBackClicked: () -> Unit, onLastTransactionClicked: () -> Unit,
    onDetailsOfTransactionsClicked: () -> Unit, onReportBasedOnTraceClicked: () -> Unit
) {
    ReportContent(
        onBackClicked = { onBackClicked() },
        onLastTransactionClicked = {onLastTransactionClicked()},
        onDetailsOfTransactionsClicked = {onDetailsOfTransactionsClicked()},
        onReportBasedOnTraceClicked = {onReportBasedOnTraceClicked()})
}

@Composable
fun ReportContent(
    onBackClicked: () -> Unit, onLastTransactionClicked: () -> Unit,
    onDetailsOfTransactionsClicked: () -> Unit, onReportBasedOnTraceClicked: () -> Unit
) {
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val lastTransaction = createRefFor("last_transaction")
            val detailsOfTransactionsId = createRefFor("details_of_transactions")
            val reportBasedOnTrace = createRefFor("report_based_on_trace")
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
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.reports), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        ReportItem(
            modifier = Modifier
                .layoutId("last_transaction"),
            backgroundImageId = R.drawable.background_last_transaction,
            title = stringResource(id = R.string.last_transaction)
        ) {
            onLastTransactionClicked()
        }
        ReportItem(
            modifier = Modifier
                .layoutId("details_of_transactions"),
            backgroundImageId = R.drawable.background_details_transaction_reports,
            title = stringResource(id = R.string.details_of_transactions)
        ) {
            onDetailsOfTransactionsClicked()
        }
        ReportItem(
            modifier = Modifier
                .layoutId("report_based_on_trace"),
            backgroundImageId = R.drawable.background_report_based_on_trace,
            title = stringResource(id = R.string.report_based_on_trace)
        ) {
            onReportBasedOnTraceClicked()
        }

    }
}

@Composable
fun ReportItem(modifier: Modifier, backgroundImageId:Int,title: String, onItemClicked: () -> Unit) {
    Box(
        modifier = modifier.padding(horizontal = START_PADDING)
            .fillMaxWidth().height(80.dp)
            .clickable { onItemClicked() }
           // .height(170.dp)
           // .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id =backgroundImageId),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth().height(100.dp),
            contentScale = ContentScale.FillBounds
        )
        Row(
            Modifier.padding(top = 20.dp).padding(horizontal = 20.dp)
                .fillMaxSize().align(Alignment.Center)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(end = 20.dp)
                   ,
                color = White100,
                style = MaterialTheme.typography.displayMedium

            )
            Spacer(modifier = Modifier.weight(1f))
            SquareRoundedButton(
                onClick = {},
                modifier = Modifier.padding(start = 26.dp)
                //.padding(end = 20.dp, start = 20.dp)
            )

        }
    }
}

@Composable
@Preview
fun ReportItemPreview() {
    TotanPayTheme {
        ReportItem(
            modifier = Modifier
                ,
            backgroundImageId = R.drawable.background_last_transaction,
            title = stringResource(id = R.string.last_transaction)
        ) {
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