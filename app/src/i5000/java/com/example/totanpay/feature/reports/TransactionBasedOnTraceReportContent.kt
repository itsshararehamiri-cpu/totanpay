package com.example.totanpay.feature.reports

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun TransactionBasedOnTraceReportContent(
    uiState: SearchTransactionUiState,
    onBackClicked: () -> Unit,
    onConfirm: (String) -> Unit,
    onPrintClicked: () -> Unit
) {
    var trace by remember {
        mutableStateOf("")
    }
    var hasError by remember {
        mutableStateOf(false)
    }
    Box(
        modifier =  Modifier.fillMaxSize() ){
        ConstraintLayout(
        ConstraintSet {
            val traceInputText = createRefFor("traceInputText")
            val result = createRefFor("mainContentReceipt")
            val print = createRefFor("print")
            val loading = createRefFor("loading")
            val inProcessing = createRefFor("inProcessing")
            val notFound = createRefFor("notFound")
            constrain(loading) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(inProcessing) {
                top.linkTo(loading.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(traceInputText) {
                top.linkTo(parent.top,40.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(result) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(print) {
                top.linkTo(result.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(notFound) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                start.linkTo(parent.start)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        if (uiState.isInitState) {
            TextInput(
                modifier = TextInputModifier
                    .layoutId("traceInputText"),
                errorMessage = stringResource(R.string.plz_enter_trace),
                title = stringResource(id = R.string.trace),
                value = trace, hasError = hasError, onNextClicked = {
                    hasError = false
                    if (trace.isNotEmpty())
                        onConfirm(trace)
                    else {
                        hasError = true
                    }
                }, isSmall = isSmall(context = LocalContext.current),onValueChange = {
                    if (!it.trim().isNotNumber())
                        trace = it.trim()
                })


        }
        if (uiState.showProgress) {
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build()

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.loading)
                        .build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("loading")
                    .fillMaxWidth()
                    .height(LOADING_HEIGHT),
                alignment = Alignment.Center
            )
            Text(
                text = stringResource(id = R.string.in_searching),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("inProcessing"),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center

            )
        }
        if (uiState.result != null && !uiState.showProgress) {
            ReceiptResultContainer(printTitle = stringResource(id = R.string.print),
                onPrintButtonClicked = { onPrintClicked() },
                onBackButtonClicked = {
                    onBackClicked()
                }) {
                ResultReceiptContainer(
                    isPaperReceipt = false,
                    modifier = Modifier.layoutId("receipt"), isSuccess = true
                ) {
                    ReceiptContent(false, uiState.result)
                }
            }
        }
        if (uiState.showNotFounding && !uiState.showProgress) {
            NotFoundTransaction(title = stringResource(id = R.string.report_based_on_trace),modifier = Modifier.layoutId("notFound")){
                onBackClicked()
            }
        }
    }
}
}

@Composable
@Preview
fun TransactionBasedOnTraceReportContentPreview() {
    TotanPayTheme {
        TransactionBasedOnTraceReportContent(
            uiState = SearchTransactionUiState(),
            onBackClicked = { },
            onConfirm = {}, onPrintClicked = {})
    }
}