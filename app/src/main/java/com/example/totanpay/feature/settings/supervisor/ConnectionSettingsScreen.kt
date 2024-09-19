package com.example.totanpay.feature.settings.supervisor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.data.util.isNumber
import com.example.totanpay.data.util.isValidIPv4
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun ConnectionSettingsScreen(viewModel: ConnectionSettingsViewModel, onBackClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackClicked()
    }
    ConnectionSettingsContent(
        uiState = uiState,
        onBackClicked = { onBackClicked() },
        onConfirm = { ip, port, nii ->
            viewModel.confirmConnectionSettings(ip, port, nii)
            onBackClicked()
        })
}

@Composable
fun ConnectionSettingsContent(
    uiState: ConnectionSettingsUiState,
    onBackClicked: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var ipValue: String by remember { mutableStateOf("") }
    var portValue: String by remember { mutableStateOf("") }
    var niiValue: String by remember { mutableStateOf("") }

    var ipErrorMessage: String by remember { mutableStateOf("") }
    var portErrorMessage: String by remember { mutableStateOf("") }
    var niiErrorMessage: String by remember { mutableStateOf("") }

    var ipHasError: Boolean by remember { mutableStateOf(false) }
    var portHasError: Boolean by remember { mutableStateOf(false) }
    var niiHasError: Boolean by remember { mutableStateOf(false) }

    var showConfirmDialog: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ipValue = uiState.ip
        portValue = uiState.port
        niiValue = uiState.nii
    }
    var modifier = Modifier.fillMaxSize()
    if (isSmall(context)) modifier = modifier.verticalScroll(rememberScrollState())
    Box(
        modifier = modifier
    ) {
        ConstraintLayout(
            constraintSet = if (!isSmall(context)) ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val ip = createRefFor("ip")
                val port = createRefFor("port")
                val nii = createRefFor("nii")
                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(ip) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(port) {
                    top.linkTo(ip.bottom)
                    end.linkTo(ip.end)
                    start.linkTo(ip.start)
                    width = Dimension.fillToConstraints
                }
                constrain(nii) {
                    top.linkTo(port.bottom)
                    end.linkTo(ip.end)
                    start.linkTo(ip.start)
                    width = Dimension.fillToConstraints
                }
                constrain(confirm) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }

            }
            else {
                ConstraintSet {
                    val toolBar = createRefFor("toolBar")
                    val ip = createRefFor("ip")
                    val port = createRefFor("port")
                    val nii = createRefFor("nii")
                    val confirm = createRefFor("confirm")
                    constrain(toolBar) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.value(0.dp)
                    }
                    constrain(ip) {
                        top.linkTo(toolBar.bottom, 35.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(port) {
                        top.linkTo(ip.bottom, 10.dp)
                        end.linkTo(ip.end)
                        start.linkTo(ip.start)
                        width = Dimension.fillToConstraints
                    }
                    constrain(nii) {
                        top.linkTo(port.bottom, 10.dp)
                        end.linkTo(ip.end)
                        start.linkTo(ip.start)
                        width = Dimension.fillToConstraints
                    }
                    constrain(confirm) {
                        top.linkTo(nii.bottom, 20.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height =
                            if (isSmall(context)) Dimension.value(0.dp) else Dimension.wrapContent

                    }
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!isSmall(context))
                BackButton(
                    title = stringResource(id = R.string.connection_settings),
                    modifier = BackButtonModifier
                        .layoutId("toolBar")
                ) {
                    onBackClicked()
                }
            TextInput(
                modifier = if (isSmall(context)) Modifier
                    .padding(start = END_PADDING, end = START_PADDING)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("ip") else TextInputModifier.layoutId("ip"),
                title = stringResource(id = R.string.ip),
                value = ipValue,
                hasError = ipHasError,
                errorMessage = ipErrorMessage,
                onNextClicked = {
                    focusManager.moveFocus(FocusDirection.Next)
                }, isSmall = isSmall(context = LocalContext.current),
                onValueChange = {
                    if (it.trim().isNumber() || it.trim().contains("."))
                        ipValue = it.trim()
                }
            )
            TextInput(
                modifier = if (isSmall(context)) Modifier
                    .padding(start = END_PADDING, end = START_PADDING)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("port") else TextInputModifier.layoutId("port"),
                title = stringResource(id = R.string.port),
                value = portValue,
                hasError = portHasError,
                errorMessage = portErrorMessage,
                onNextClicked = {
                    focusManager.moveFocus(FocusDirection.Next)
                }, isSmall = isSmall(context),
                onValueChange = {
                    if (!it.trim().isNotNumber())
                        portValue = it.trim()
                }
            )
            TextInput(
                modifier = if (isSmall(context)) Modifier
                    .padding(start = END_PADDING, end = START_PADDING)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("nii") else TextInputModifier.layoutId("nii"),
                title = stringResource(id = R.string.nii),
                value = niiValue,
                hasError = niiHasError,
                errorMessage = niiErrorMessage,
                onNextClicked = {
                    keyboard?.hide()
                    if (isSmall(context)) {
                        ipHasError = false
                        portHasError = false
                        niiHasError = false
                        if (ipValue.isNotEmpty() && portValue.isNotEmpty() && niiValue.isNotEmpty()) {
                            showConfirmDialog = true
                        } else {
                            if (ipValue.isEmpty())
                                ipHasError = true
                            if (portValue.isEmpty())
                                portHasError = true
                            if (niiValue.isEmpty())
                                niiHasError = true
                        }
                    }
                }, isSmall = isSmall(context),
                onValueChange = {
                    if (!it.trim().isNotNumber())
                        niiValue = it.trim()
                })
            if (!isSmall(context))
                MainButton(
                    modifier = Modifier
                        .mainButtonModifier(isSmall = isSmall(context = LocalContext.current))
                        .layoutId("confirm")
                ) {
                    ipHasError = false
                    portHasError = false
                    niiHasError = false
                    ipErrorMessage = ""
                    portErrorMessage = ""
                    niiErrorMessage = ""
                    if (ipValue.isNotEmpty() && portValue.isNotEmpty() && niiValue.isNotEmpty()) {
                        if (isValidIPv4(ipValue))
                            showConfirmDialog = true
                        else {
                            ipHasError = true
                            ipErrorMessage = context.getString(R.string.plz_enter_an_valid_ip)
                        }
                    } else {
                        if (ipValue.isEmpty()) {
                            ipHasError = true
                            ipErrorMessage = context.getString(R.string.plz_enter_ip)
                        }
                        if (portValue.isEmpty()) {
                            portHasError = true
                            portErrorMessage = context.getString(R.string.plz_enter_port)
                        }
                        if (niiValue.isEmpty()) {
                            niiHasError = true
                            niiErrorMessage = context.getString(R.string.plz_enter_nii)
                        }
                    }
                }
        }
        if (showConfirmDialog)
            ConfirmDialog(
                isSmall = isSmall(context = LocalContext.current),
                onConfirmButtonClicked = {
                    onConfirm(ipValue, portValue, niiValue)
                },
                onCancelButtonClicked = { showConfirmDialog = false })
    }
}