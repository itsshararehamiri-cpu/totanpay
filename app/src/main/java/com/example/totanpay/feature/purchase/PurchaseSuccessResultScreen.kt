package com.example.totanpay.feature.purchase

import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddPurchaseId
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.data.repository.PrintStatus
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Black
import com.example.totanpay.ui.theme.Green60
import com.example.totanpay.ui.theme.TotanPayTheme
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
    LaunchedEffect(uiState.responseForCallerApp) {
        if (uiState.responseForCallerApp != null && !packageName.isNullOrEmpty()) {
            delay(1000)
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            intent?.putExtra("transaction", uiState.responseForCallerApp)
            (context as MainActivity).setResult(RESULT_OK, intent)
            context.finish()
        }
    }
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
    BackHandler {
        onBackButtonClicked()
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null) ReceiptUi(content = {
        ReceiptContent(true, uiState.result, printForCustomer)
    }) {
        receiptBitmap = it
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context)
            viewModel.changePrintStatus()
            startPrint = false
            printCount++
        }
    }
    LaunchedEffect(receiptBitmap) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context)
            viewModel.changePrintStatus()
            startPrint = false
            printCount++
        }
    }
    if (uiState.result != null) {
        ReceiptResultContainer(printTitle = stringResource(
            id = if (printCount != 0)
                R.string.print_merchant_receipt else R.string.print_customer_receipt
        ),
            onPrintButtonClicked = { startPrint = true },
            onBackButtonClicked = {
                onBackButtonClicked()
            }) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = true
            ) {
                ReceiptContent(false, uiState.result, printForCustomer = true)
            }
        }
    }
}

@Composable
fun ReceiptContent(
    isPaperReceipt: Boolean, result: ResponseTransaction?, printForCustomer: Boolean = true
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
        if(isSmall(context))
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
            isPaperReceipt
        )
        AddTypeDateTime(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            type = result.transactionType,
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
                result.amount
                ,textColor=if(isPaperReceipt) Black else Green60
                ,isPaperReceipt
            )
        if (isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(), color = firstColor, isPaperReceipt = true
            )
        }

    }
}

@Composable
@Preview
fun ReceiptContentPreveiw() {
    TotanPayTheme {
        ReceiptContent(
            true, result = ResponseTransaction(
                responseCode = "00",
                responseMessage = "خطا",
                rrn = "1",
                trace = "2",
                merchantName = "تست",
                merchantId = "3",
                merchantPhone = "0214236598",
                terminalID = "2",
                transactionType = "خرید",
                date = "1403/10/08",
                time = "10:22",
                issuerName = "صادرات",
                amount = "10000",
                availableBalance = "10000",
                maskedPan = "6037********78",
                realBalance = "13",
                voucherPin = "12"
            ), printForCustomer = true
        )
    }
}

@Composable
@Preview
fun PurchaseSuccessResultPreveiw() {
    TotanPayTheme {
        // PurchaseSuccessResult("")
    }
}