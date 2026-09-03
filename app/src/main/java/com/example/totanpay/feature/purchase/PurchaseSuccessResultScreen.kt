package com.example.totanpay.feature.purchase

import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.MainActivity
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.TIME_TO_FINISH_SUCCESS_RESULT
import com.example.totanpay.common.CountdownEffect
import com.example.totanpay.common.PlaybackSoundEffect
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.receipt.AddAmount
import com.example.totanpay.common.receipt.AddCustomerSignature
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddPurchaseId
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddReceiptType
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.receipt.ShowSuccessResult
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Black
import com.example.totanpay.ui.theme.Green60
import kotlinx.coroutines.delay

@Composable
fun PurchaseSuccessResult(
    viewModel: PurchaseSuccessResultViewModel,
    response: String,
    packageName: String?,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    if (packageName != null) {
        viewModel.getResponseFroCallerApp(response)
    }
    LaunchedEffect(uiState.responseForCallerApp) {// TODO:
        if (uiState.responseForCallerApp != null && !packageName.isNullOrEmpty()) {
            delay(1000)
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            intent?.putExtra("transaction", uiState.responseForCallerApp)
            (context as MainActivity).setResult(RESULT_OK, intent)
            context.finish()
        }
    }
    var customerReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var merchantReceiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var printForMerchant by remember { mutableStateOf(false) }
    var printForCustomer by remember { mutableStateOf(false) }
    PlaybackSoundEffect(uiState.playbackSound, R.raw.successfultransaction)
    LaunchedEffect(true) {
        viewModel.init(response)
    }
    LaunchedEffect(uiState.result) {
        if (uiState.result != null) viewModel.getPrintStatus()
    }
    BackHandler {
        onBackButtonClicked()
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null) {
        if (customerReceiptBitmap == null)
            ReceiptUi(content = {
                ReceiptContent(true, uiState.result, ReceiptType.CUSTOMER_RECEIPT)
            }) {
                customerReceiptBitmap = it
            }
        if (merchantReceiptBitmap == null)
            ReceiptUi(content = {
                ReceiptContent(true, uiState.result, ReceiptType.MERCHANT_RECEIPT)
            }) {
                merchantReceiptBitmap = it
            }
    }

    LaunchedEffect(
        customerReceiptBitmap,
        printForCustomer
    ) {
        if (
            customerReceiptBitmap != null &&
           printForCustomer
        ) {
            printForCustomer=false
            viewModel.printCustomerReceipt(customerReceiptBitmap!!, context)
            printForCustomer=false
        }
    }
    LaunchedEffect(merchantReceiptBitmap, printForMerchant) {
        if (
            merchantReceiptBitmap != null &&
            printForMerchant) {
            printForMerchant=false
            viewModel.printMerchantReceipt(merchantReceiptBitmap!!, context)
            printForMerchant=false
        }
    }
    LaunchedEffect(uiState.autoPrintCustomerReceipt) {
        if (uiState.autoPrintCustomerReceipt) {
            printForCustomer = true
        }
    }
    if (uiState.result != null) {
        ReceiptResultContainer(
            showCustomerPrintButton =
                uiState.showPrintForCustomer,
            showMerchantPrintButton =
                uiState.showPrintForMerchant,
            printTitle = stringResource(id = R.string.print_customer_receipt),
            errorInPrint = uiState.errorInPrint,
            onMerchantPrintButtonClicked = { printForMerchant = true },
            onCustomerPrintButtonClicked = { printForCustomer = true },
            clearPrintErrorMessage = {
                viewModel.clearErrorMessage()
            },
            onBackButtonClicked = {
                onBackButtonClicked()
            }) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = true
            ) {
                ReceiptContent(false, uiState.result, ReceiptType.CUSTOMER_RECEIPT)
            }
        }
    }
}

@Composable
fun ReceiptContent(
    isPaperReceipt: Boolean, result: ResponseTransaction?, receiptType: ReceiptType,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.containerReceiptModifier(isPaperReceipt, context)
    ) {
        val firstColor = if (isPaperReceipt) Color.Black else MaterialTheme.colorScheme.onSurface
        if (!isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(), color = firstColor, isPaperReceipt = false
            )
        }
        if (isPaperReceipt)
            AddReceiptType(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                receiptType = receiptType,
                textColor = firstColor
            )
        if (isSmall(context))
            AddAmount(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result!!.amount,
                textColor = Green60,
                isPaperReceipt
            )
        AddMerchantNamePhone(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            textColor = firstColor,
            englishMerchantName = result.englishMerchantName,
            isPaperReceipt = isPaperReceipt
        )
        AddTypeDateTime(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            type = context.getString(result.transactionType),
            date = result.date,
            time = result.time,
            textColor = firstColor,
            isPaperReceipt
        )
        HorizontalDivider(
            isPaperReceipt = isPaperReceipt
        )
        AddMerchantIdTerminalId(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor,
            isPaperReceipt
        )
        if (!result.posCode.isNullOrEmpty()) AddPosCode(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            result.posCode,
            firstColor,
            isPaperReceipt
        )
        if (!result.purchaseId.isNullOrEmpty()) AddPurchaseId(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            purchaseId = result.purchaseId,
            textColor = firstColor,
            isPaperReceipt
        )
        AddMaskedPanCardIssuer(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            maskedPan = result.maskedPan.mask(),
            cardIssuer = result.issuerName,
            textColor = firstColor,
            isPaperReceipt
        )
        AddRRNStan(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor,
            isPaperReceipt
        )
        if (isPaperReceipt || !isSmall(context))
            AddAmount(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result.amount, textColor = if (isPaperReceipt) Black else Green60, isPaperReceipt
            )
        if (isPaperReceipt) {
            ShowSuccessResult(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally), firstColor = firstColor
            )
            if (receiptType == ReceiptType.MERCHANT_RECEIPT)
                AddCustomerSignature(
                    modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                    textColor = firstColor,
                    isPaperReceipt = true
                )
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(), color = firstColor, isPaperReceipt = true
            )
        }

    }
}
