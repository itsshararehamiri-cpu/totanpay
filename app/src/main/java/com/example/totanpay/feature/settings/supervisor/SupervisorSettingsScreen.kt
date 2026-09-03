package com.example.totanpay.feature.settings.supervisor

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.RowReceipt
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.data.repository.device.KCV
import com.example.totanpay.receipt.ConfigurationReceiptContent
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.theme.Black

@Composable
fun SupervisorSettingsScreen(
    viewModel: SupervisorSettingsViewModel,
    onBackClicked: () -> Unit,
    onConnectionSettingsClicked: () -> Unit,
    onKeyInjectionSettingsClicked: () -> Unit
) {

    val isFarsi = LocalLanguageState.current.isFarsiSelected.value
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
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
    var kcvBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var showKcv: Boolean by remember {
        mutableStateOf(false)
    }
    if (uiState.kcv != null) {
        ReceiptUi(content = {
            KcvReceiptContent(uiState.kcv!!)
        }) {
            kcvBitmap = it
        }

    }
    LaunchedEffect(kcvBitmap) {
        if (kcvBitmap != null) {
            viewModel.print(kcvBitmap!!, context, onSuccess = {
                kcvBitmap = null

            }, onFailed = {
                kcvBitmap = null
                //  onBackClicked()
            })
        }
    }
    if (uiState.configurationResult != null)
    {
        ReceiptUi(content = {
            CompositionLocalProvider(LocalLayoutDirection provides if (isFarsi) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                ConfigurationReceiptContent(uiState.configurationResult!!)
            }
        }) {
            receiptBitmap = it
        }
    }
    LaunchedEffect(receiptBitmap) {
        if (receiptBitmap != null) {
            viewModel.print(receiptBitmap!!, context, onSuccess = {
                receiptBitmap = null
                kcvBitmap = null
                if (showKcv)
                    viewModel.getKcv()
            }, onFailed = {
                showErrorInPrint = true
                errorMessagePrint = it
                kcvBitmap = null
                if (showKcv)
                    viewModel.getKcv()
            })
        }
    }
    SupervisorSettingsContent(
        uiState = uiState,
        showErrorInPrint = showErrorInPrint,
        errorMessagePrint = errorMessagePrint,
        onBackClicked = { onBackClicked() },
        onConnectionSettingsClicked = { onConnectionSettingsClicked() },
        onKeyInjectionSettingsClicked = {
            onKeyInjectionSettingsClicked()
        },
        setTaxForIrancellCharge = {
            viewModel.setTaxForIrancellCharge(it)
        },
        onConfigurationClicked = {
            showKcv=true
            viewModel.configuration() },
        onEndShowPrintErrorMessage = {
            showErrorInPrint = false
            errorMessagePrint = ""
        },
        onEnableAndDisableMace = {
            viewModel.enableMac(it)
        },
        hideKeyIsNotInjectedError = { viewModel.hideKeyIsNotInjectedError() },
        onGetTerminalInfoClicked = {
            showKcv=false
            viewModel.getTerminalInfo()
        })
}


@Composable
fun KcvReceiptContent(kcv: KCV) {
    val context = LocalContext.current

    Column(
        Modifier.containerReceiptModifier(true, context)
    ) {
        val modifier = Modifier.rowReceiptModifier(true)
        val textColor = Color.Black
        if (kcv.master.isNotEmpty()) {
            RowReceipt(
                modifier = modifier,
                second = "KCV Master",
                first = kcv.master,
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (kcv.data.isNotEmpty()) {
            RowReceipt(
                modifier = modifier,
                first = kcv.data,
                second = "KCV Working Data",
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (kcv.mac.isNotEmpty()) {
            RowReceipt(
                modifier = modifier,
                first = kcv.mac,
                second = "KCV Working Mac",
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (kcv.pin.isNotEmpty()) {
            RowReceipt(
                modifier = modifier,
                first = kcv.pin,
                second = "KCV Working Pin",
                textColor = textColor, isPaperReceipt = true
            )
        }
        AddPSPLog(
            modifier = Modifier.fillMaxWidth(),
            color = Black,
            isPaperReceipt = true
        )
    }
}