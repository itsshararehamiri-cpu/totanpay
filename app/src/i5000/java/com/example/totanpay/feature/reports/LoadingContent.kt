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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.totanpay.common.receipt.AddNumberOfAllTransactions
import com.example.totanpay.common.receipt.AddSumOfAllTransactions
import com.example.totanpay.common.selectedTransactionTypesOf
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.PrintButton
import com.example.totanpay.ui.component.report.TransactionCardCarousel
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT
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
    var showDetails by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val loading = createRefFor("loading")
                val inProcessing = createRefFor("inProcessing")
                val list = createRefFor("list")
                val filterSummary = createRefFor("filterSummary")
                val notFound = createRefFor("notFound")
                val printSum = createRefFor("printSum")
                val printAll = createRefFor("printAll")
                val showMore=createRefFor("showMore")
                val showSum=createRefFor("showSum")
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
                constrain(filterSummary) {
                    top.linkTo(parent.top, 16.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                constrain(list) {
                    top.linkTo(filterSummary.bottom, 8.dp)
                    bottom.linkTo(printAll.top, 6.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.fillToConstraints
                }
                constrain(notFound) {
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(sumOfTransactions) {
                    top.linkTo(filterSummary.bottom, 20.dp)
                    end.linkTo(parent.end, 12.dp)
                    start.linkTo(parent.start, 12.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(numberOfTransactions) {
                    bottom.linkTo(sumOfTransactions.top, 5.dp)
                    end.linkTo(parent.end, 12.dp)
                    start.linkTo(parent.start, 12.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(showMore) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end,12.dp)
                    start.linkTo(printSum.end, 4.dp)
                    width =if(!showDetails) Dimension.fillToConstraints else Dimension.value(0.dp)
                }
                constrain(printSum) {
                    bottom.linkTo(showMore.bottom)
                    top.linkTo(showMore.top)
                    start.linkTo(parent.start,12.dp)
                    end.linkTo(showMore.start, 4.dp)
                    width =if(!showDetails) Dimension.fillToConstraints else Dimension.value(0.dp)
                }
                constrain(printAll) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end,12.dp)
                    start.linkTo(showSum.end, 4.dp)
                    width =if(showDetails) Dimension.fillToConstraints else Dimension.value(0.dp)
                }
                constrain(showSum) {
                    bottom.linkTo(printAll.bottom)
                    top.linkTo(printAll.top)
                    start.linkTo(parent.start,12.dp)
                    end.linkTo(printAll.start, 4.dp)
                    width =if(showDetails) Dimension.fillToConstraints else Dimension.value(0.dp)
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

                if (!uiState.result.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .layoutId("filterSummary")
                    ) {
                        val context=LocalContext.current
                        val types = selectedTransactionTypesOf(uiState.selectedTransactionTypes)
                        val typesLabel = types.joinToString("، ") { context.getString(it.title) }
                        val hasDateFilter = uiState.fromDate.isNotEmpty() && uiState.toDate.isNotEmpty()
                        val hasAmountFilter = (uiState.fromAmount.isNotEmpty() && uiState.fromAmount != "0") ||
                                (uiState.toAmount.isNotEmpty() && uiState.toAmount != "-1")
                        if (typesLabel.isNotEmpty() || hasDateFilter || hasAmountFilter) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                if (typesLabel.isNotEmpty())
                                    Text(
                                        text = "${stringResource(id = R.string.transaction_type)}: $typesLabel",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                if (hasDateFilter)
                                    Text(
                                        text = "${stringResource(id = R.string.from_date)} ${uiState.fromDate} ${uiState.fromTime} " +
                                                "${stringResource(id = R.string.to_date)} ${uiState.toDate} ${uiState.toTime}",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                if (hasAmountFilter)
                                    Text(
                                        text = "${stringResource(id = R.string.from_amount)} ${uiState.fromAmount.formatAmount()} ${stringResource(id = R.string.to_amount)} ${uiState.toAmount.formatAmount()}",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                            }
                        }
                    }
                    if (showDetails) {
                        TransactionCardCarousel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .layoutId("list"),
                            reports = uiState.result
                        )
                    }
                    if(!showDetails)
                    {
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
                    }
                    if (showDetails)
                        PrintButton(
                            title = stringResource(R.string.print_all),
                            modifier =
                            Modifier.padding(horizontal = 10.dp)
                                .height(BUTTON_HEIGHT)
                                .fillMaxWidth()
                                .layoutId("printAll")

                        ) {
                            onPrintAllClicked()
                        }
                    PrintButton(
                        title = stringResource(R.string.print_sum),
                        modifier =
                        Modifier.padding(horizontal = 10.dp)
                            .height(BUTTON_HEIGHT)
                            .fillMaxWidth()
                            .layoutId("printSum")

                    ) {
                        onPrintSumClicked()
                    }
                    PrintButton(
                        title = stringResource(R.string.show_more),
                        modifier =
                        Modifier.padding(horizontal = 10.dp)
                            .height(BUTTON_HEIGHT)
                            .fillMaxWidth()
                            .layoutId("showMore")

                    ) {
                        showDetails=true
                    }
                    PrintButton(
                        title = stringResource(R.string.show_sum),
                        modifier =
                        Modifier.padding(horizontal = 10.dp)
                            .height(BUTTON_HEIGHT)
                            .fillMaxWidth()
                            .layoutId("showSum")

                    ) {
                        showDetails=false
                    }
                }
                if (uiState.showNotFound) {
                    NotFoundTransaction(
                        title = stringResource(id = R.string.details_of_transactions),
                        modifier = Modifier.layoutId("notFound")
                    ) {
                        onBackClicked()
                    }
                }


            }
        }
        if (showErrorInPrint)
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = errorMessagePrint.ifEmpty { "خطا در چاپ" }
            ) {
                onEndShowPrintErrorMessage()
            }
    }
}

@Composable
@Preview
fun LoadingContentPreview() {
    TotanPayTheme {
    }
}
