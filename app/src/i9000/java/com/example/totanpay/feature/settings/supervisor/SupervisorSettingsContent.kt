package com.example.totanpay.feature.settings.supervisor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.compound.SettingsItem
import com.example.totanpay.ui.component.dialog.MessageDialog
import com.example.totanpay.ui.component.dialog.SetTaxForChargeDialog
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE
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
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        taxForIrancellCharge = uiState.taxForIrancellCharge
    }
    LaunchedEffect(uiState.macIsEnabled) {
        macIsEnabled = uiState.macIsEnabled
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val connectionSettings = createRefFor("connectionSettings")
                val keyInjection = createRefFor("keyInjection")
                val setTaxForIrancellCharge = createRefFor("setTaxForIrancellCharge")
                val enableMac = createRefFor("enableMac")
                val configuration = createRefFor("configuration")
                val getTerminalInfoItem = createRefFor("getTerminalInfoItem")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(connectionSettings) {
                    top.linkTo(toolBar.bottom, 30.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(keyInjection) {
                    top.linkTo(connectionSettings.bottom, 3.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(setTaxForIrancellCharge) {
                    top.linkTo(keyInjection.bottom, 3.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(enableMac) {
                    top.linkTo(setTaxForIrancellCharge.bottom, 3.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(configuration) {
                    top.linkTo(enableMac.bottom, 3.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(getTerminalInfoItem) {
                    top.linkTo(configuration.bottom, 3.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.supervisor_settings),
                modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("connectionSettings"),
                iconImageId = R.drawable.mirroring_screen,
                title = stringResource(id = R.string.connection_settings),
                backgroundIconId = R.drawable.mirroring_screen_,
                isSmall = isSmall(context),
                backgroundImageId = R.drawable.ic_item_settings1
            ) {
                onConnectionSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier.layoutId("keyInjection"),
                iconImageId = R.drawable.ic_key_injection,
                title = stringResource(id = R.string.key_injection),
                backgroundIconId = R.drawable.backgound_key_injection,
                isSmall = isSmall(context),
                backgroundImageId = R.drawable.ic_item_settings2
            ) {
                onKeyInjectionSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier.layoutId("setTaxForIrancellCharge"),
                iconImageId = R.drawable.s,
                title = stringResource(id = R.string.set_percent_of_tax_irancell_charge),
                backgroundIconId = R.drawable.sss,
                isSmall = isSmall(context),
                backgroundImageId = R.drawable.ic_item_settings2
            ) {
                showSetTaxForIrancellChargeDialog = true
            }
            Box(
                modifier = Modifier
                    .layoutId("enableMac")
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_item_settings2),
                    contentDescription = "",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
                Row(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .padding(top = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.background_change_merchant_password),
                            contentDescription = "",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_playback_sound),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.enable_mac_on_key_injection),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displayMedium

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        modifier = Modifier.padding(end = 30.dp, top = 0.dp),
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
            }

            SettingsItem(
                modifier = Modifier
                    .layoutId("configuration"),
                backgroundImageId = R.drawable.ic_item_settings2,
                iconImageId = R.drawable.ic_configuration,
                backgroundIconId = R.drawable.backgound_configuration,
                isSmall = isSmall(context),
                title = stringResource(id = R.string.configuration)
            ) {
                onConfigurationClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("getTerminalInfoItem"),
                backgroundImageId = R.drawable.ic_item_settings_4,
                iconImageId = R.drawable.ic_configuration,
                backgroundIconId = R.drawable.backgound_configuration,
                isSmall = isSmall(context),
                title = stringResource(id = R.string.get_terminal_info)
            ) {
                onGetTerminalInfoClicked()
            }
        }
        Image(
            painter = painterResource(id = R.drawable.totan),
            contentDescription = "",
            modifier = Modifier
                .size(TOTAN_ICON_SIZE)
                .align(Alignment.BottomCenter)
        )
        if (uiState.showConfigurationIsSucceed) {
            MessageDialog(
                isVisible = uiState.showConfigurationIsSucceed,
                message = stringResource(R.string.configuration_do_successfully),
                imageId = R.drawable.aa
            )
        }
        if (uiState.error!=R.string.empty_message) {
            MessageDialog(
                isVisible = (uiState.error!=R.string.empty_message),
                message =stringResource( uiState.error),
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
        if (showErrorInPrint) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = errorMessagePrint
            ) {
                onEndShowPrintErrorMessage()
            }
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