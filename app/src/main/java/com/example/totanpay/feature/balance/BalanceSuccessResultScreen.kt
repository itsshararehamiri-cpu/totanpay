package com.example.totanpay.feature.balance

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.TIME_TO_FINISH_SUCCESS_RESULT
import com.example.totanpay.common.CountdownEffect
import com.example.totanpay.common.PlaybackSoundEffect
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.receipt.AddAvailableBalance
import com.example.totanpay.common.receipt.AddBalance
import com.example.totanpay.common.receipt.AddFee
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.Black
import com.example.totanpay.ui.theme.Green60

@Composable
fun BalanceSuccessResultScreen(
    viewModel: BalanceSuccessResultViewModel,
    response: String,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    var showErrorInPrint by remember {
        mutableStateOf(false)
    }
    var errorMessagePrint by remember {
        mutableStateOf("")
    }
    LaunchedEffect(uiState.printStatus) {
        if (uiState.printStatus== PrintStatus.PRINT) {
            startPrint = true
        }
    }
    PlaybackSoundEffect(uiState.playbackSound, R.raw.successfultransaction)
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    if (uiState.result != null) {
        ReceiptUi(content = {
            BalanceReceipt(isPaperReceipt = true, result = uiState.result)
        }) {
            receiptBitmap = it
        }
    }
    LaunchedEffect(startPrint,receiptBitmap) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context, onSuccess = {
            }, onFailed = {
                showErrorInPrint = true
                errorMessagePrint = it
            })
            startPrint = false
        }
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            ReceiptResultContainer(showMerchantPrintButton = false,
                showCustomerPrintButton = true,
                printTitle = stringResource(R.string.print),
                errorInPrint = errorMessagePrint,
                onBackButtonClicked = {
                    onBackButtonClicked()
                },
                onCustomerPrintButtonClicked = {
                    startPrint = true
                },
                onMerchantPrintButtonClicked = {

                },
                clearPrintErrorMessage = {
                    errorMessagePrint=""
                }) {
                ResultReceiptContainer(
                    isPaperReceipt = false,
                    modifier = Modifier.layoutId("receipt"), isSuccess = true
                ) {
                    BalanceReceipt(false, uiState.result)
                }
            }
            if (showErrorInPrint)
                ShowToast(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    message = errorMessagePrint) {
                    showErrorInPrint = false
                    errorMessagePrint = ""
                }
        }
    }
}


@Composable
fun BalanceReceipt(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.containerReceiptModifier(isPaperReceipt, context)
    ) {
        val firstColor = if (isPaperReceipt) Color.Black else MaterialTheme.colorScheme.onSurface
        val modifierRowReceipt = Modifier.rowReceiptModifier(isPaperReceipt)
        if (!isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = false
            )
        }
        if (isSmall(context) && !isPaperReceipt) {
            AddAvailableBalance(
                modifier = modifierRowReceipt,
                result!!.availableBalance!!,
                isPaperReceipt = false, textColor = Black
            )
            AddBalance(
                modifier = modifierRowReceipt,
                result.realBalance!!,
                isPaperReceipt = false, textColor = Green60
            )
        }
        AddMerchantNamePhone(
            modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            englishMerchantName = result.englishMerchantName,
            textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        AddTypeDateTime(
            modifier = modifierRowReceipt,
            type =context.getString( TransactionType.BALANCE.title),
            date = result.date,
            time = result.time,
            textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        HorizontalDivider(
            isPaperReceipt = isPaperReceipt
        )
        AddMerchantIdTerminalId(
            modifier = modifierRowReceipt,
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        if (!result.posCode.isNullOrEmpty())
            AddPosCode(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result.posCode,
                firstColor,
                isPaperReceipt
            )
        AddMaskedPanCardIssuer(
            modifier = modifierRowReceipt,
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        AddRRNStan(
            modifier = modifierRowReceipt,
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor, isPaperReceipt = isPaperReceipt
        )
        if (!isSmall(context) || isPaperReceipt) {
            AddAvailableBalance(
                modifier = modifierRowReceipt,
                result.availableBalance!!,
                isPaperReceipt = isPaperReceipt,
                textColor = firstColor
            )
            AddBalance(
                modifier = modifierRowReceipt,
                result.realBalance!!,
                textColor = if (isPaperReceipt) Black else Green60, isPaperReceipt = isPaperReceipt
            )
        }
        AddFee(
            modifier = modifierRowReceipt
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally),
            "1800",
            firstColor, isPaperReceipt = isPaperReceipt
        )
        if (isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = true
            )
        }
    }
}


