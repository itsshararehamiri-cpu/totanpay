package com.example.totanpay.feature.settings.supervisor

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.receipt.ConfigurationReceiptContent
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.compound.SettingsItem
import com.example.totanpay.ui.component.dialog.MessageDialog
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE

@Composable
fun SupervisorSettingsScreen(
    viewModel: SupervisorSettingsViewModel,
    onBackClicked: () -> Unit,
    onConnectionSettingsClicked: () -> Unit,
    onKeyInjectionSettingsClicked: () -> Unit
) {
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
    if (uiState.configurationResult != null)
        ReceiptUi(content = {
            ConfigurationReceiptContent(uiState.configurationResult!!)
        }) {
            receiptBitmap = it
        }
    LaunchedEffect(receiptBitmap) {
        if (receiptBitmap != null) {
            viewModel.print(receiptBitmap!!, context, onSuccess = {
                receiptBitmap = null
            }, onFailed = {
                showErrorInPrint = true
                errorMessagePrint = it
            })
        }
    }
    SupervisorSettingsContent(uiState = uiState,
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
        onConfigurationClicked = { viewModel.configuration() },
        onEndShowPrintErrorMessage = {
            showErrorInPrint = false
            errorMessagePrint = ""
        }, hideKeyIsNotInjectedError = {viewModel.hideKeyIsNotInjectedError()})
}


