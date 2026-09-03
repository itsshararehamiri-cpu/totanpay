package com.example.totanpay.feature.settings.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.totanpay.R
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.compound.I5000SettingsItem
import com.example.totanpay.ui.component.dialog.MessageDialog
import com.example.totanpay.ui.component.dialog.SetTaxForChargeDialog
import com.example.totanpay.ui.theme.Green50

@Composable
fun SupervisorSettingsContent(
    uiState: SupervisorSettingsUiState,
    errorMessagePrint: String,
    showErrorInPrint: Boolean,
    onBackClicked: () -> Unit,
    onConnectionSettingsClicked: () -> Unit,
    onKeyInjectionSettingsClicked: () -> Unit,
    onConfigurationClicked: () -> Unit,
    onGetTerminalInfoClicked: () -> Unit,
    setTaxForIrancellCharge: (String) -> Unit,
    onEndShowPrintErrorMessage: () -> Unit,
    onEnableAndDisableMace: (Boolean) -> Unit,
    hideKeyIsNotInjectedError: () -> Unit

) {
    var showSetTaxForIrancellChargeDialog by remember {
        mutableStateOf(false)
    }
    var taxForIrancellCharge by remember {
        mutableStateOf("")
    }
    var macIsEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        taxForIrancellCharge = uiState.taxForIrancellCharge
    }
    LaunchedEffect(uiState.macIsEnabled) {
        macIsEnabled = uiState.macIsEnabled
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(top = 28.dp, bottom = 20.dp)
        ) {
            I5000SettingsItem(
                modifier = Modifier,
                iconImageId = R.drawable.ic_i5000_connection_settings,
                title = stringResource(id = R.string.connection_settings)
            ) {
                onConnectionSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier,
                iconImageId = R.drawable.ic_i5000_key_injection,
                title = stringResource(id = R.string.key_injection)
            ) {
                onKeyInjectionSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier,
                iconImageId = null,
                title = stringResource(id = R.string.set_percent_of_tax_irancell_charge)
            ) {
                showSetTaxForIrancellChargeDialog = true
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.enable_mac_on_key_injection),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = macIsEnabled,
                    onCheckedChange = {
                        macIsEnabled = it
                        onEnableAndDisableMace(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Green50
                    )
                )
            }
            I5000SettingsItem(
                modifier = Modifier,
                iconImageId = R.drawable.ic_i5000_configuration_settings,
                title = stringResource(id = R.string.configuration)
            ) {
                onConfigurationClicked()
            }
            I5000SettingsItem(
                modifier = Modifier,
                iconImageId = null,
                title = stringResource(id = R.string.get_terminal_info)
            ) {
                onGetTerminalInfoClicked()
            }
        }
        if (uiState.showConfigurationIsSucceed) {
            MessageDialog(
                isVisible = uiState.showConfigurationIsSucceed,
                message = stringResource(R.string.configuration_do_successfully),
                imageId = R.drawable.aa
            )
        }
        if (uiState.error != R.string.empty_message) {
            MessageDialog(
                isVisible = (uiState.error != R.string.empty_message),
                message = stringResource(uiState.error),
                imageId = R.drawable.uns
            )
        }
        if (uiState.showConfigurationLoading) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Loading(stringResource(R.string.in_configuration))
            }
        }
        if (uiState.showGetTerminalInfoLoading) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Loading(stringResource(R.string.in_get_terminal_info))
            }
        }
        if (showErrorInPrint)
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = errorMessagePrint.ifEmpty { "خطا در چاپ" }
            ) {
                onEndShowPrintErrorMessage()
            }
        if (uiState.showKeyIsNotInjectedError) {
            ShowToast(
                modifier = Modifier.align(Alignment.TopCenter),
                message = stringResource(R.string.key_injection_not_done)
            ) {
                hideKeyIsNotInjectedError()
            }
        }
        if (showSetTaxForIrancellChargeDialog) {
            SetTaxForChargeDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                onConfirmButtonClicked = {
                    taxForIrancellCharge = it
                    showSetTaxForIrancellChargeDialog = false
                    if (taxForIrancellCharge.isNotEmpty()) {
                        setTaxForIrancellCharge(taxForIrancellCharge.toEnglishNumber())
                    }
                },
                onCancelButtonClicked = {
                    taxForIrancellCharge = ""
                    showSetTaxForIrancellChargeDialog = false
                })
        }
    }
}
