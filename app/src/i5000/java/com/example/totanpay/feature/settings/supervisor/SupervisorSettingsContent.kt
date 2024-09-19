package com.example.totanpay.feature.settings.supervisor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.compound.I5000SettingsItem
import com.example.totanpay.ui.component.dialog.MessageDialog

@Composable
fun SupervisorSettingsContent(
    uiState: SupervisorSettingsUiState,
    errorMessagePrint: String,
    showErrorInPrint: Boolean,
    onBackClicked: () -> Unit,
    onConnectionSettingsClicked: () -> Unit,
    onKeyInjectionSettingsClicked: () -> Unit,
    onConfigurationClicked: () -> Unit,
    onEndShowPrintErrorMessage: () -> Unit

) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val connectionSettings = createRefFor("connectionSettings")
                val keyInjection = createRefFor("keyInjection")
                val configuration = createRefFor("configuration")
                constrain(connectionSettings) {
                    top.linkTo(parent.top,28.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(keyInjection) {
                    top.linkTo(connectionSettings.bottom, 10.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(configuration) {
                    top.linkTo(keyInjection.bottom, 10.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll( rememberScrollState())

        ) {

            I5000SettingsItem(
                modifier = Modifier
                    .layoutId("connectionSettings"),
                iconImageId = R.drawable.ic_i5000_connection_settings,
                title = stringResource(id = R.string.connection_settings)
            ) {
                onConnectionSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier.layoutId("keyInjection"),
                iconImageId = R.drawable.ic_i5000_key_injection,
                title = stringResource(id = R.string.key_injection)
            ) {
                onKeyInjectionSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier
                    .layoutId("configuration"),
                iconImageId = R.drawable.ic_i5000_configuration_settings,
                title = stringResource(id = R.string.configuration)
            ) {
                onConfigurationClicked()
            }
        }
        if (uiState.showConfigurationIsSucceed) {
            MessageDialog(
                isVisible = uiState.showConfigurationIsSucceed,
                message = stringResource(R.string.configuration_do_successfully),
                imageId = R.drawable.aa
            )
        }
        if (uiState.error.isNotEmpty()) {
            MessageDialog(
                isVisible = uiState.error.isNotEmpty(),
                message = uiState.error,
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
        if (showErrorInPrint)
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = errorMessagePrint.ifEmpty { "خطا در چاپ" }
            ) {
                onEndShowPrintErrorMessage()
            }
    }
}