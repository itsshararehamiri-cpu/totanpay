package com.example.totanpay.feature.reports

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.ReportAll
import com.example.totanpay.ui.component.ReportSum
import com.google.gson.Gson
import saman.zamani.persiandate.PersianDate

@Composable
fun ResultDetailsTransactionScreen(
    viewModel: DetailsTransactionViewModel,
    fromDate: String?,
    toDate: String?,
    fromAmount: String?,
    toAmount: String?,
    selectedTransactions: String?,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val from: DateContainer? = if (!fromDate.isNullOrEmpty()) Gson().fromJson(
        fromDate,
        DateContainer::class.java
    ) else null
    val to: DateContainer? =
        if (!toDate.isNullOrEmpty()) Gson().fromJson(toDate, DateContainer::class.java) else null
    var sumReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var allReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrintSumReceipt by remember {
        mutableStateOf(false)
    }
    var startPrintAllReceipt by remember {
        mutableStateOf(false)
    }
    var showErrorInPrint by remember {
        mutableStateOf(false)
    }
    var errorMessagePrint by remember {
        mutableStateOf("")
    }
    BackHandler {
        onBackClicked()
    }
    if (uiState.sumOfTransactions.isNotEmpty() && uiState.numberOfTransactions.isNotEmpty()
        && uiState.terminalId.isNotEmpty() && uiState.merchantName.isNotEmpty()
    )
        ReceiptUi(content = {
            ReportSum(
                merchantName = uiState.merchantName,
                merchantPhone = uiState.merchantPhone,
                sumOfAllTransactions = uiState.sumOfTransactions,
                fromDate = uiState.fromDate,
                toDate = uiState.toDate,
                fromTime = uiState.fromTime,
                toTime = uiState.toTime,
                numberOfAllTransactions = uiState.numberOfTransactions, englishMerchantName = uiState.englishMerchantName
            )

        }) {
            sumReceiptBitmap = it
        }
    if (!uiState.result.isNullOrEmpty())
        ReceiptUi(content = {
            ReportAll(
                uiState.result!!,uiState.sumOfTransactions
            )
        }) {
            allReceiptBitmap = it
        }
    BackHandler {
        onBackClicked()
    }
    if (sumReceiptBitmap != null && startPrintSumReceipt) {
        viewModel.print(
            bitmap = sumReceiptBitmap!!,
            context = LocalContext.current,
            onSuccess = {},
            onFailed = {
                showErrorInPrint = true
                errorMessagePrint = it
            })
        startPrintSumReceipt = false
    }
    if (allReceiptBitmap != null && startPrintAllReceipt) {
        viewModel.print(bitmap = allReceiptBitmap!!, context = LocalContext.current,
            onSuccess = {}, onFailed = {
                showErrorInPrint = true
                errorMessagePrint = it
            })
        startPrintAllReceipt = false
    }
    LaunchedEffect(Unit) {
        val fromDate1 = if (from == null) null else PersianDate().apply {
            setShYear((from.year.ifEmpty { "0" }).toInt())
            setShMonth((from.month.ifEmpty { "0" }).toInt())
            setShDay((from.day.ifEmpty { "0" }).toInt())
            hour = (from.hour.ifEmpty { "0" }).toInt()
            minute = (from.minute.ifEmpty { "0" }).toInt()
            second = 0
        }
        val toDate1 = if (to == null) null else PersianDate().apply {
            setShYear((to.year.ifEmpty { "0" }).toInt())
            setShMonth((to.month.ifEmpty { "0" }).toInt())
            setShDay((to.day.ifEmpty { "0" }).toInt())
            hour = (to.hour.ifEmpty { "0" }).toInt()
            minute = (to.minute.ifEmpty { "0" }).toInt()
            second = 0
        }
        viewModel.search(fromDate1, toDate1, fromAmount, toAmount, selectedTransactions)
    }
    LoadingContent(uiState, showErrorInPrint, errorMessagePrint, onBackClicked = {
        onBackClicked()
    }, onPrintSumClicked = {
        startPrintSumReceipt = true
    }, onPrintAllClicked = {
        startPrintAllReceipt = true
    }, onEndShowPrintErrorMessage = {
        showErrorInPrint = false
        errorMessagePrint = ""
    })
}


