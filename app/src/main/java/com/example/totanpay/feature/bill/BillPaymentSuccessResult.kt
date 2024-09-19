package com.example.totanpay.feature.bill

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.TIME_TO_FINISH_SUCCESS_RESULT
import com.example.totanpay.common.CountdownEffect
import com.example.totanpay.common.PlaybackSoundEffect
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.data.repository.PrintStatus
import com.example.totanpay.receipt.ReceiptUi

@Composable
fun BillPaymentSuccessResult(
    viewModel: BillPaymentSuccessResultViewModel, response: String, onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var printForCustomer: Boolean by remember {
        mutableStateOf(true)
    }
    var printCount: Int by remember {
        mutableIntStateOf(0)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    PlaybackSoundEffect(uiState.playbackSound, R.raw.successfultransaction)
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    LaunchedEffect(uiState.printStatus) {
        startPrint = when (uiState.printStatus) {
            PrintStatus.NO_PRINTING -> false
            PrintStatus.ALWAYS_PRINTING -> true
            else -> false
        }
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null)
        ReceiptUi(content = {
            BillPaymentReceiptContent(
                isPaperReceipt = true,
                result = uiState.result,
                printForCustomer
            )

        }) {
            receiptBitmap = it
        }
    LaunchedEffect(receiptBitmap) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context)
            viewModel.changePrintStatus()
            startPrint = false
            printCount++
        }
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context)
            viewModel.changePrintStatus()
            startPrint = false
            printCount++
        }
    }
    if (uiState.result != null) {
        ReceiptResultContainer(
            printTitle = stringResource(R.string.print_customer_receipt),
            onPrintButtonClicked = {
                startPrint = true
            },
            onBackButtonClicked = { onBackButtonClicked() }) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = true
            ) {
                BillPaymentReceiptContent(isPaperReceipt = false, result = uiState.result, printForCustomer)
            }
        }
    }
}