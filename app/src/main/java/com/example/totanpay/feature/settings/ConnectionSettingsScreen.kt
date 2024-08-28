package com.example.totanpay.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun ConnectionSettingsScreen(viewModel: ConnectionSettingsViewModel, onBackClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackClicked()
    }
    ConnectionSettingsContent(uiState=uiState,onBackClicked = { onBackClicked() }, onConfirm = { ip, port, nii ->
        viewModel.confirmConnectionSettings(ip, port, nii)
        onBackClicked()
    })
}


@Composable
fun ConnectionSettingsContent(uiState: ConnectionSettingsUiState,
    onBackClicked: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var ipValue: String by remember { mutableStateOf("") }
    var portValue: String by remember { mutableStateOf("") }
    var niiValue: String by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        ipValue=uiState.ip
        portValue=uiState.port
        niiValue=uiState.nii
    }
    BackHandler {
        onBackClicked()
    }
    ConstraintLayout(
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

        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.connection_settings), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("ip"),
            title = stringResource(id = R.string.ip),
            trailerTitle = "",
            value = ipValue
        ) {
            ipValue = it
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("port"),
            title = stringResource(id = R.string.port),
            trailerTitle = "",
            value = portValue
        ) {
            portValue = it
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("nii"),
            title = stringResource(id = R.string.nii),
            trailerTitle = "",
            value = niiValue
        ) {
            niiValue = it
        }
        MainButton(
            title = "تایید",
            modifier = Modifier
                .padding(
                    bottom = MARGIN_BOTTOM_MAIN_CONFIRM,
                    start = END_PADDING, end = START_PADDING, top = 16.dp
                )

                .layoutId("confirm")
                .clickable {
                    if (ipValue.isNotEmpty() && portValue.isNotEmpty() && niiValue.isNotEmpty())
                        onConfirm(ipValue, portValue, niiValue)
                }
                .padding(top = 18.dp)
                .fillMaxWidth()

        )
    }
}