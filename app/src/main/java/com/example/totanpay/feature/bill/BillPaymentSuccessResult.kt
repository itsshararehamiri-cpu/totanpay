package com.example.totanpay.feature.bill

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.receipt.ReceiptUi

@Composable
fun BillPaymentSuccessResult(
    viewModel: BillPaymentSuccessResultViewModel, response: String, onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var customerReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var merchantReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var printForCustomer by remember { mutableStateOf(false) }
    var printForMerchant by remember { mutableStateOf(false) }
    PlaybackSoundEffect(uiState.playbackSound, R.raw.successfultransaction)
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    LaunchedEffect(uiState.result) {
        if (uiState.result != null) viewModel.getPrintStatus()
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null) {
        if (customerReceiptBitmap == null) {
            ReceiptUi(content = {
                BillPaymentReceiptContent(
                    isPaperReceipt = true,
                    result = uiState.result,
                    ReceiptType.CUSTOMER_RECEIPT
                )
            }) {
                customerReceiptBitmap = it
            }
        }
        if (merchantReceiptBitmap == null) {
            ReceiptUi(content = {
                BillPaymentReceiptContent(
                    isPaperReceipt = true,
                    result = uiState.result,
                    ReceiptType.MERCHANT_RECEIPT
                )
            }) {
                merchantReceiptBitmap = it
            }
        }
    }
    LaunchedEffect(customerReceiptBitmap, printForCustomer) {
        if (customerReceiptBitmap != null && printForCustomer) {
            printForCustomer = false
            viewModel.printCustomerReceipt(customerReceiptBitmap!!, context)
        }
    }
    LaunchedEffect(merchantReceiptBitmap, printForMerchant) {
        if (merchantReceiptBitmap != null && printForMerchant) {
            printForMerchant = false
            viewModel.printMerchantReceipt(merchantReceiptBitmap!!, context)
        }
    }
    LaunchedEffect(uiState.autoPrintCustomerReceipt) {
        if (uiState.autoPrintCustomerReceipt) {
            printForCustomer = true
        }
    }
    if (uiState.result != null) {
        ReceiptResultContainer(
            showCustomerPrintButton = uiState.showPrintForCustomer,
            showMerchantPrintButton = uiState.showPrintForMerchant,
            printTitle = stringResource(R.string.print_customer_receipt),
            errorInPrint = uiState.errorInPrint,
            onCustomerPrintButtonClicked = { printForCustomer = true },
            onMerchantPrintButtonClicked = { printForMerchant = true },
            clearPrintErrorMessage = { viewModel.clearErrorMessage() },
            onBackButtonClicked = { onBackButtonClicked() }
        ) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = true
            ) {
                BillPaymentReceiptContent(
                    isPaperReceipt = false,
                    result = uiState.result,
                    ReceiptType.CUSTOMER_RECEIPT
                )
            }
        }
    }
}