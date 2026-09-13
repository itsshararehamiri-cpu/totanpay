package com.example.totanpay.feature.reports

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.common.selectedTransactionTypesOf
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.button.ReportOptionFilter
import com.example.totanpay.ui.component.button.SelectedFilterChip
import com.example.totanpay.ui.component.dialog.SelectAmountInReportDialog
import com.example.totanpay.ui.component.dialog.SelectDateTimeInReportDialog
import com.example.totanpay.ui.component.dialog.SelectTransactionTypeInReport
import com.example.totanpay.ui.theme.BUTTON_CORNER_RADIUS
import com.example.totanpay.ui.theme.TotanPayTheme
import com.google.gson.Gson

@Composable
fun DetailTransactionReportContent(
    onBackButtonClicked: () -> Unit,
    searchTransaction: (from: String, to: String, fromAmount: String, toAmount: String, selectedTransactions: String) -> Unit
) {

    val scrollState = rememberScrollState()
    BackHandler {
        onBackButtonClicked()
    }
    var selectedTransactionType by remember {
        mutableStateOf("")
    }
    var fromAmountValue by remember {
        mutableStateOf("")
    }
    var toAmountValue by remember {
        mutableStateOf("")
    }

    var selectFromDateTimeValue by remember {
        mutableStateOf("")
    }
    var selectToDateTimeValue by remember {
        mutableStateOf("")
    }

    var isTransactionTypeSelected by remember {
        mutableStateOf(false)
    }
    var isAmouteSelected by remember {
        mutableStateOf(false)
    }
    var isDateTimeSelected by remember {
        mutableStateOf(false)
    }
    var isTransactionTypeSelected2 by remember {
        mutableStateOf(false)
    }
    var isAmouteSelected2 by remember {
        mutableStateOf(false)
    }
    var isDateTimeSelected2 by remember {
        mutableStateOf(false)
    }
    var showToast by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val typeLabelText = remember(selectedTransactionType) {
        selectedTransactionTypesOf(selectedTransactionType)
    }.joinToString("، ") { context.getString(it.title) }
    val fromDateDisplay = remember(selectFromDateTimeValue) {
        selectFromDateTimeValue.takeIf { it.isNotEmpty() }
            ?.let { runCatching { Gson().fromJson(it, DateContainer::class.java).toDisplayText() }.getOrNull() }
    }
    val toDateDisplay = remember(selectToDateTimeValue) {
        selectToDateTimeValue.takeIf { it.isNotEmpty() }
            ?.let { runCatching { Gson().fromJson(it, DateContainer::class.java).toDisplayText() }.getOrNull() }
    }
    val fromAmountLabel = stringResource(R.string.from_amount)
    val toAmountLabel = stringResource(R.string.to_amount)
    val transactionTypeLabel = stringResource(R.string.transaction_type)


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ConstraintLayout(
                ConstraintSet {
                    val reportFilter = createRefFor("reportFilter")
                    val loading = createRefFor("loading")
                    val inProcessing = createRefFor("in_processing")
                    val confirm=createRefFor("confirm")
                    constrain(reportFilter) {
                        top.linkTo(parent.top, 24.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
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
                    constrain(confirm) {
                        bottom.linkTo(parent.bottom, 20.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                },
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(top = 16.dp, bottom = 16.dp)
                    .padding(bottom = 70.dp)
            ) {
                LazyRow(
                    modifier = Modifier.layoutId("reportFilter"),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    item {
                        ReportOptionFilter(selected = isTransactionTypeSelected,
                            title = stringResource(R.string.transaction_type),
                            modifier = Modifier.padding(horizontal = 7.dp),
                            onSelected = {
                                isTransactionTypeSelected2 = true
                            })
                    }
                    item {
                        ReportOptionFilter(selected = isDateTimeSelected,
                            title = stringResource(R.string.date_time),
                            modifier = Modifier.padding(horizontal = 7.dp),
                            onSelected = {
                                isDateTimeSelected2 = true
                            })
                    }
                    item {
                        ReportOptionFilter(selected = isAmouteSelected,
                            title = stringResource(R.string.amount),
                            modifier = Modifier.padding(horizontal = 7.dp),
                            onSelected = {
                                isAmouteSelected2 = true
                            })
                    }
                }

            }
            if (isTransactionTypeSelected || isDateTimeSelected || isAmouteSelected) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isTransactionTypeSelected && typeLabelText.isNotEmpty()) {
                        SelectedFilterChip(label = "$transactionTypeLabel: $typeLabelText") {
                            selectedTransactionType = ""
                            isTransactionTypeSelected = false
                        }
                    }
                    if (isDateTimeSelected && fromDateDisplay != null && toDateDisplay != null) {
                        SelectedFilterChip(label = "$fromDateDisplay - $toDateDisplay") {
                            selectFromDateTimeValue = ""
                            selectToDateTimeValue = ""
                            isDateTimeSelected = false
                        }
                    }
                    if (isAmouteSelected && (fromAmountValue.isNotEmpty() || toAmountValue.isNotEmpty())) {
                        SelectedFilterChip(
                            label = "$fromAmountLabel ${fromAmountValue.ifEmpty { "0" }} $toAmountLabel ${toAmountValue.ifEmpty { "-" }}"
                        ) {
                            fromAmountValue = ""
                            toAmountValue = ""
                            isAmouteSelected = false
                        }
                    }
                }
            }
            Box(
                modifier =  Modifier.
                mainButtonModifier(isSmall = isSmall(context = LocalContext.current) )
                    .clip(RoundedCornerShape(BUTTON_CORNER_RADIUS))
                    .background( MaterialTheme.colorScheme.primary)
                    .clip(RoundedCornerShape(BUTTON_CORNER_RADIUS))
                    .clickable {
                        val inclusiveToDateTime = selectToDateTimeValue.takeIf { it.isNotEmpty() }
                            ?.let {
                                runCatching {
                                    Gson().toJson(Gson().fromJson(it, DateContainer::class.java).plusOneMinute())
                                }.getOrDefault(it)
                            } ?: selectToDateTimeValue
                        searchTransaction(selectFromDateTimeValue,inclusiveToDateTime,fromAmountValue,toAmountValue,selectedTransactionType)}
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.show_result),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
        if (isDateTimeSelected2) {
            SelectDateTimeInReportDialog(
                onCancelButtonClicked = {
                    isDateTimeSelected2=false
                }, confirmDateTime = { fromDate, toDate ->
                    selectToDateTimeValue = toDate
                    selectFromDateTimeValue = fromDate
                    isDateTimeSelected = true
                    isDateTimeSelected2=false
                })
        }
        if(isAmouteSelected2){
            SelectAmountInReportDialog(
                onCancelButtonClicked = {                    isAmouteSelected2=false
                }, confirmAmount = { fromAmount,toAmount ->
                    fromAmountValue = fromAmount
                    toAmountValue = toAmount
                    isAmouteSelected = true
                    isAmouteSelected2=false
                })
        }
        if(isTransactionTypeSelected2){
            SelectTransactionTypeInReport(
                onCancelButtonClicked = {                    isTransactionTypeSelected2=false
                }, onNoTypeSelected = {
                    showToast = true
                }, confirmTransactionType = {
                    selectedTransactionType=it
                    isTransactionTypeSelected = true
                    isTransactionTypeSelected2=false
                })
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = stringResource(R.string.no_transactions_selected)
            ) {
                showToast = false
            }
        }
    }

}


@Composable
@Preview
fun DetailTransactionReportContentPreview() {
    TotanPayTheme {
        DetailTransactionReportContent(onBackButtonClicked = {}) { from, to, fromAmount, toAmount, selectedTransactions ->
        }
    }
}