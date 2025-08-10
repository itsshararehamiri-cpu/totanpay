package com.example.totanpay.feature.balance

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.TIME_TO_FINISH_SUCCESS_RESULT
import com.example.totanpay.common.CountdownEffect
import com.example.totanpay.common.PlaybackSoundEffect
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.receipt.getFontSize
import com.example.totanpay.common.receipt.getFontSizeUnSuccess
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.Dimensions.LINE_HEIGHT_PAPER_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.LINE_HEIGHT_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_ROW_PAGER_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_ROW_RECEIPT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun BalanceUnSuccessResultScreen(
    viewModel: BalanceUnSuccessResultViewModel,
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
    PlaybackSoundEffect(uiState.playbackSound, R.raw.unsuccessfultransaction)
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    if (uiState.result != null)
        ReceiptUi(content = {
            UnSuccessReceiptContent(isPaperReceipt = true, uiState.result)
        }) {
            receiptBitmap = it
        }
    LaunchedEffect(startPrint,receiptBitmap) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(
                bitmap = receiptBitmap!!,
                context = context,
                onSuccess = {},
                onFailed = {
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
            ReceiptResultContainer(
                printTitle = stringResource(id = R.string.print_customer_receipt),
                onPrintButtonClicked = {
                    startPrint = true
                },
                onBackButtonClicked = { onBackButtonClicked() }) {
                ResultReceiptContainer(
                    isPaperReceipt = false,
                    modifier = Modifier.layoutId("receipt"), isSuccess = false
                ) {
                    UnSuccessReceiptContent(false, uiState.result)
                }
            }
            if (showErrorInPrint)
                ShowToast(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    message = errorMessagePrint.ifEmpty { "خطا در چاپ" }
                ) {
                    showErrorInPrint = false
                    errorMessagePrint = ""
                }
        }
    }
}

@Composable
fun UnSuccessReceiptContent(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .containerReceiptModifier(isPaperReceipt, context)
    ) {
        val modifierRowReceipt = Modifier.rowReceiptModifier(isPaperReceipt)
        val firstColor = if (isPaperReceipt) Color.Black else MaterialTheme.colorScheme.onSurface
        if (!isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = false
            )
        }
        AddMerchantNamePhone(
            modifier = Modifier.fillMaxWidth(),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            textColor = firstColor, isPaperReceipt
        )
        AddTypeDateTime(
            modifier = modifierRowReceipt,
            type = TransactionType.BALANCE.title,
            date = result.date,
            time = result.time,
            textColor = firstColor, isPaperReceipt
        )
        HorizontalDivider(
            isPaperReceipt = isPaperReceipt
        )
        AddMerchantIdTerminalId(
            modifier = modifierRowReceipt,
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor, isPaperReceipt
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
            textColor = firstColor, isPaperReceipt
        )
        AddRRNStan(
            modifier = modifierRowReceipt,
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor, isPaperReceipt
        )
        Text(
            text = stringResource(id = R.string.unsuccess_transaction),
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSizeUnSuccess(isPaperReceipt, context),
                fontWeight = FontWeight.Bold
            ),
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = if (isPaperReceipt) 0.dp else 4.dp)
                .fillMaxWidth()
                .padding(top = if (isPaperReceipt) MARGIN_TOP_ROW_PAGER_RECEIPT else MARGIN_TOP_ROW_RECEIPT),
            color = firstColor, textAlign = TextAlign.Center
        )
        Text(
            text = "${result.responseMessage} ${result.responseCode}",
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context), fontWeight = FontWeight.Bold,
                lineHeight = if (isPaperReceipt) LINE_HEIGHT_PAPER_RECEIPT else LINE_HEIGHT_RECEIPT
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = if (isPaperReceipt) 0.dp else 4.dp)
                .fillMaxWidth()
                .padding(top = if (isPaperReceipt) MARGIN_TOP_ROW_PAGER_RECEIPT else MARGIN_TOP_ROW_RECEIPT),
            color = firstColor, textAlign = TextAlign.Center
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


@Composable
@Preview
fun UnSuccessReceiptContentPreveiw() {
    TotanPayTheme {
        UnSuccessReceiptContent(
            true, result = ResponseTransaction(
                responseCode = "-1",
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
                availableBalance = "10000", maskedPan = "6037********78",
                realBalance = "13", voucherPin = "12"
            )
        )
    }
}