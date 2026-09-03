package com.example.totanpay.feature.reports

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.receipt.AddNumberOfAllTransactions
import com.example.totanpay.common.receipt.AddSumOfAllTransactions
import com.example.totanpay.ui.PrintButtonModifier
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.PrintButton
import com.example.totanpay.ui.component.report.DetailsReportOfDayItem
import com.example.totanpay.ui.component.report.HeaderRow
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun LoadingContent(
    uiState: DetailsTransactionUiState,
    showErrorInPrint: Boolean, errorMessagePrint: String,
    onBackClicked: () -> Unit,
    onPrintSumClicked: () -> Unit, onPrintAllClicked: () -> Unit,
    onEndShowPrintErrorMessage: () -> Unit
) {
    Box(
        modifier =  Modifier.fillMaxSize() ){
        ConstraintLayout(
            ConstraintSet {
                val loading = createRefFor("loading")
                val inProcessing = createRefFor("inProcessing")
                val list = createRefFor("list")
                val toolBar = createRefFor("toolBar")
                val header = createRefFor("header")
                val notFound = createRefFor("notFound")
                val printSum = createRefFor("printSum")
                val printAll = createRefFor("printAll")
                val sumOfTransactions = createRefFor("sumOfTransactions")
                val numberOfTransactions = createRefFor("numberOfTransactions")
                constrain(loading) {
                    top.linkTo(parent.top, 90.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(inProcessing) {
                    top.linkTo(loading.bottom, 20.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(header) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(list) {
                    top.linkTo(header.bottom, 0.dp)
                    bottom.linkTo(numberOfTransactions.top, 16.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.fillToConstraints
                }
                constrain(notFound) {
                    top.linkTo(toolBar.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(sumOfTransactions) {
                    bottom.linkTo(printAll.top, 10.dp)
                    end.linkTo(list.end, 12.dp)
                    start.linkTo(list.start, 12.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(numberOfTransactions) {
                    bottom.linkTo(sumOfTransactions.top, 5.dp)
                    end.linkTo(list.end, 12.dp)
                    start.linkTo(list.start, 12.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(printAll) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(list.end,12.dp)
                    start.linkTo(printSum.end, 4.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(printSum) {
                    bottom.linkTo(printAll.bottom)
                    top.linkTo(printAll.top)
                    start.linkTo(list.start,12.dp)
                    end.linkTo(printAll.start, 4.dp)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.showProgress) {
                val imageLoader = ImageLoader.Builder(LocalContext.current)
                    .components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build()
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = R.drawable.loading)
                            .apply(block = fun ImageRequest.Builder.() {
                            }).build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .layoutId("loading")
                        .fillMaxWidth()
                        .height(LOADING_HEIGHT)
                )
                Text(
                    text = stringResource(id = R.string.in_searching),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("inProcessing"),
                    style = MaterialTheme.typography.displayMedium
                )
            } else {
                BackButton(
                    title = stringResource(id = R.string.details_of_transactions),
                    modifier = BackButtonModifier
                        .layoutId("toolBar")
                ) {
                    onBackClicked()
                }
                if (!uiState.result.isNullOrEmpty()) {
                    HeaderRow(
                        modifier = Modifier.layoutId("header"), listOf(
                            stringResource(id = R.string.transaction_type),
                            stringResource(id = R.string.amount),
                            stringResource(id = R.string.trace),
                            stringResource(id = R.string.time),
                            stringResource(id = R.string.row1)
                        ), isPaperReceipt = false, textColor = MaterialTheme.colorScheme.onSurface
                    )
                    DetailsReportOfDayItem(
                        Modifier
                            .fillMaxWidth()
                            .layoutId("list"),
                        uiState.result,
                        isPaperReceipt = false,
                        textColor = MaterialTheme.colorScheme.onSurface,
                    )
                    AddNumberOfAllTransactions(
                        Modifier
                            .fillMaxWidth()
                            .layoutId("numberOfTransactions"),
                        number = uiState.numberOfTransactions,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        isPaperReceipt = false
                    )
                    AddSumOfAllTransactions(
                        Modifier
                            .fillMaxWidth()
                            .layoutId("sumOfTransactions"),
                        sum = uiState.sumOfTransactions,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        isPaperReceipt = false
                    )
                    PrintButton(
                        title = stringResource(R.string.print_all),
                        modifier =
                        PrintButtonModifier
                            .layoutId("printAll")

                    ) {
                        onPrintAllClicked()
                    }
                    PrintButton(
                        title = stringResource(R.string.print_sum),
                        modifier =
                        PrintButtonModifier
                            .layoutId("printSum")

                    ) {
                        onPrintSumClicked()
                    }
                }
                if (uiState.showNotFound) {
                    NotFoundTransaction(title = stringResource(id = R.string.details_of_transactions),modifier = Modifier.layoutId("notFound")){
                        onBackClicked()
                    }
                }


            }
        }
        if (showErrorInPrint)
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = errorMessagePrint
            ) {
                onEndShowPrintErrorMessage()
            }
    }
}
