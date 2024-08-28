package com.example.totanpay.feature.reports

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.feature.purchase.PurchaseSuccessReceipt
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import org.jpos.transaction.TransactionManagerMBean

@Composable
fun TransactionBasedOnTraceReportScreen(
    viewModel: SearchTransactionViewModel,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TransactionBasedOnTraceReportContent(
        uiState = uiState,
        onBackClicked = { onBackClicked() },
        onConfirm = {
            Log.d("", "TransactionBasedOnTraceReportScreen() called")
            viewModel.search(it)
        })
}

@Composable
fun TransactionBasedOnTraceReportContent(
    uiState: PurchaseSuccessResultUiState,
    onBackClicked: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var trace by remember {
        mutableStateOf("")
    }
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val traceInputText = createRefFor("traceInputText")
            val confirm = createRefFor("confirm")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(traceInputText) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(confirm) {
                top.linkTo(traceInputText.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }

        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.report_based_on_trace), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("traceInputText"),
            title = stringResource(id = R.string.trace),
            trailerTitle = "",
            value = trace
        ) {
            trace = it
        }

        MainButton(
            title = stringResource(id = R.string.display_results),
            modifier = Modifier
                .padding(
                    bottom = MARGIN_BOTTOM_MAIN_CONFIRM,
                    start = END_PADDING, end = START_PADDING, top = 16.dp
                )

                .layoutId("confirm")
                .padding(top = 18.dp)
                .fillMaxWidth().clickable {   onConfirm(trace)}

        ){
            Log.d("TAG", "TransactionBasedOnTraceReportContent() called->$trace")
            onConfirm(trace)
        }
        if (uiState.result != null)
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Background)
            ) {
                if (uiState.result != null)
                    PurchaseSuccessReceipt(isPaperReceipt = false, uiState.result)
                MainButton(
                    title = "چاپ رسید مشتری", modifier = Modifier
                        .padding(top = 18.dp)
                        .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth()

                ) {
                    println("yyyyyyyyyyyyyyyy")

                }
            }
    }
}

@Composable
@Preview
fun TransactionBasedOnTraceReportContentPreview() {
    TotanPayTheme {
        TransactionBasedOnTraceReportContent(
            uiState = PurchaseSuccessResultUiState(),
            onBackClicked = { },
            onConfirm = {})
    }
}