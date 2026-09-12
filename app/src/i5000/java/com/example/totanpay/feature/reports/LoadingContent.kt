package com.example.totanpay.feature.reports

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.PrintButton
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.TotanPayTheme
import org.json.JSONObject

private fun selectedTransactionTypesOf(json: String): List<TransactionType> {
    if (json.isEmpty()) return emptyList()
    return try {
        val obj = JSONObject(json)
        buildList {
            if (obj.optString("purchaseType") == "has") add(TransactionType.PURCHASE)
            if (obj.optString("billPayType") == "has") add(TransactionType.BILL_PAY)
            if (obj.optString("voucherType") == "has") add(TransactionType.VOUCHER)
            if (obj.optString("topupType") == "has") add(TransactionType.TOPUP)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

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
                val toolBar = createRefFor("toolBar")
                val filterSummary = createRefFor("filterSummary")
                val header = createRefFor("header")
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
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(filterSummary) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                constrain(header) {
                    top.linkTo(filterSummary.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(list) {
                    top.linkTo(header.bottom, 0.dp)
                    bottom.linkTo(printAll.top, 6.dp)
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
                    BackButton(
                        title = stringResource(id = R.string.details_of_transactions),
                        modifier = BackButtonModifier
                            .layoutId("toolBar")
                    ) {
                        onBackClicked()
                    }
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
                        Text(
                            text = "${stringResource(id = R.string.details_of_transactions)} (${uiState.numberOfTransactions})",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .layoutId("header")
                        )
                        TransactionCardList(
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
private fun TransactionCardList(
    modifier: Modifier,
    reports: List<ResponseTransaction>
) {
    val context = LocalContext.current
    val groupedTransactions = reports.groupBy { it.date }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedTransactions.forEach { (date, transactions) ->
            item {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }
            items(transactions) { transaction ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = context.getString(transaction.transactionType),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = transaction.amount.formatAmount(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.trace)}: ${transaction.trace}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = transaction.time,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun LoadingContentPreview() {
    TotanPayTheme {
    }
}
