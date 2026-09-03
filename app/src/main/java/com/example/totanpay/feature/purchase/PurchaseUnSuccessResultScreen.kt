package com.example.totanpay.feature.purchase

import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.MainActivity
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
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Dimensions.LINE_HEIGHT_PAPER_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.LINE_HEIGHT_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_ROW_PAGER_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_ROW_RECEIPT
import kotlinx.coroutines.delay

@Composable
fun PurchaseUnSuccessResult(
    viewModel: PurchaseUnSuccessResultViewModel,
    response: String,
    packageName: String?,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFarsi = LocalLanguageState.current.isFarsiSelected.value
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(true)
    }
    PlaybackSoundEffect(uiState.playbackSound, R.raw.unsuccessfultransaction)
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
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    if (uiState.result != null) {
        ReceiptUi(content = {
            CompositionLocalProvider(LocalLayoutDirection provides if (isFarsi) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                UnSuccessReceiptContent(true, uiState.result)
            }
        }) {
            receiptBitmap = it
        }
    }
    LaunchedEffect(startPrint, receiptBitmap) {
        if (receiptBitmap != null && startPrint) {
            viewModel.printAndConfirm(bitmap = receiptBitmap!!, context = context)
            startPrint = false
        }
    }
    CountdownEffect(TIME_TO_FINISH_SUCCESS_RESULT) {
        onBackButtonClicked()
    }
    if (uiState.result != null) {
        ReceiptResultContainer(
            showCustomerPrintButton = false,
            showMerchantPrintButton = false,
            errorInPrint = uiState.errorInPrint,
            printTitle = stringResource(id = R.string.print),
            onCustomerPrintButtonClicked = {
                startPrint = true
            },
            onMerchantPrintButtonClicked = {},
            onBackButtonClicked = {
                onBackButtonClicked()
            },
            clearPrintErrorMessage = {
                viewModel.clearErrorMessage()
            }) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = false
            ) {
                UnSuccessReceiptContent(false, uiState.result)
            }
        }
    }
}

@Composable
fun UnSuccessReceiptContent(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    val context = LocalContext.current

    val modifierRowReceipt = Modifier.rowReceiptModifier(isPaperReceipt)
    Column(
        modifier = Modifier.containerReceiptModifier(isPaperReceipt, context)
    ) {
        val firstColor =
            if (isPaperReceipt) Color.Black else MaterialTheme.colorScheme.onSurface
        if (!isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(), color = firstColor, isPaperReceipt = false
            )
        }
        AddMerchantNamePhone(
            modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
            merchantName = result!!.merchantName,
            englishMerchantName = result!!.englishMerchantName,

            merchantPhone = result.merchantPhone,
            textColor = firstColor,
            isPaperReceipt
        )
        AddTypeDateTime(
            modifier = modifierRowReceipt,
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
            modifier = modifierRowReceipt,
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
        AddMaskedPanCardIssuer(
            modifier = modifierRowReceipt,
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor,
            isPaperReceipt
        )
        AddRRNStan(
            modifier = modifierRowReceipt,
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor,
            isPaperReceipt
        )
        if (result.responseCode != "-1") {
            Text(
                text = if(result.responseCode.toInt()>0)"${stringResource(id = R.string.unsuccess_transaction)} - ${result.responseCode}"
                else stringResource(id = R.string.unsuccess_transaction),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSizeUnSuccess(isPaperReceipt, context),
                    fontWeight = FontWeight.Bold
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            result.responseMessage?.let {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = getFontSize(isPaperReceipt, context),
                        lineHeight = if (isPaperReceipt) LINE_HEIGHT_PAPER_RECEIPT else LINE_HEIGHT_RECEIPT
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = if (isPaperReceipt) 0.dp else 4.dp)
                        .fillMaxWidth()
                        .padding(top = if (isPaperReceipt) MARGIN_TOP_ROW_PAGER_RECEIPT else MARGIN_TOP_ROW_RECEIPT),
                    color = firstColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
//            Text(
//                text = "${stringResource(id = R.string.amount)} ${result.amount}",
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    fontSize = getFontSize(isPaperReceipt, context),
//                    fontWeight = FontWeight.Bold
//                ),
//                modifier = Modifier
//                    .wrapContentWidth()
//                    .align(Alignment.CenterHorizontally),
//                color = firstColor.copy(alpha = 0.72f),
//            )
            Text(
                text = stringResource(R.string.receipt_payback_message_part1),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context)
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text = stringResource(R.string.receipt_payback_message_part2),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context)
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text = stringResource(R.string.receipt_payback_message_part3),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context)
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text = stringResource(R.string.receipt_payback_message_part4_s),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context)
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text = stringResource(R.string.receipt_payback_message_part5_s),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context)
                ),
                modifier = modifierRowReceipt
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
        }
        if (isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(), color = firstColor, isPaperReceipt = true
            )
        }
    }
}