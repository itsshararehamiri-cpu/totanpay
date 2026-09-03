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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.receipt.ReceiptUi

@Composable
fun TransactionBasedOnTraceReportScreen(
    viewModel: SearchTransactionViewModel,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    BackHandler {
        onBackButtonClicked()
    }
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    var errorInPrint by remember {
        mutableStateOf("")
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context, onSuccess = {}, onFailed = {
                errorInPrint=it
            })
            startPrint = false
        }
    }
    if (uiState.result != null) {
        ReceiptUi(content = {
                ReceiptContent(true, uiState.result,ReceiptType.CUSTOMER_RECEIPT)
        }) {
            receiptBitmap = it
        }
    }
    TransactionBasedOnTraceReportContent(errorInPrint,
        uiState = uiState,
        onBackClicked = { onBackButtonClicked() },
        onConfirm = {trace,traceIsSelected->
            keyboard?.hide()
            viewModel.search(trace,traceIsSelected)
        },
        clearPrintErrorMessage={
            errorInPrint=""
        },
        onPrintClicked = {
            startPrint = true
        })
}

