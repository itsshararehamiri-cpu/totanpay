package com.example.totanpay.feature.settings

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.BlurredDialog
import com.example.totanpay.ui.component.MessageDialog
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.White300

@Composable
fun SupervisorSettingsScreen(
    viewModel: SupervisorSettingsViewModel,
    onBackClicked: () -> Unit, onConnectionSettingsClicked: () -> Unit
) {
    BackHandler {
        onBackClicked()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SupervisorSettingsContent(uiState = uiState,
        onBackClicked = { onBackClicked() },
        onConnectionSettingsClicked = { onConnectionSettingsClicked() },
        onKeyInjectionSettingsClicked = { viewModel.keyInjection() },
        onConfigurationClicked = { viewModel.configuration() })
}

@Composable
fun SupervisorSettingsContent(
    uiState: SupervisorSettingsUiState,
    onBackClicked: () -> Unit, onConnectionSettingsClicked: () -> Unit,
    onKeyInjectionSettingsClicked: () -> Unit, onConfigurationClicked: () -> Unit
) {
    Box {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val connectionSettings = createRefFor("connectionSettings")
                val keyInjection = createRefFor("keyInjection")
                val configuration = createRefFor("configuration")
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
                    top.linkTo(connectionSettings.bottom, 30.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(configuration) {
                    top.linkTo(keyInjection.bottom, 30.dp)
                    end.linkTo(connectionSettings.end)
                    start.linkTo(connectionSettings.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            BackButton(
                title = stringResource(id = R.string.supervisor_settings), modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("connectionSettings"),
                iconImageId = R.drawable.ic_settings_connection,
                title = stringResource(id = R.string.connection_settings)
            ) {
                onConnectionSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("keyInjection"),
                iconImageId = R.drawable.ic_key_injection_settings,
                title = stringResource(id = R.string.key_injection)
            ) {
                onKeyInjectionSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("configuration"),
                iconImageId = R.drawable.ic_configuration_settings,
                title = stringResource(id = R.string.configuration)
            ) {
                onConfigurationClicked()
            }


        }
        if (uiState.success)
            MessageDialog(
                isVisible = uiState.success,
                message = "پیکربندی با موفقیت انجام شد",
                imageId = R.drawable.aa
            )
        if (uiState.error.isNotEmpty())
            MessageDialog(
                isVisible = uiState.error.isNotEmpty(),
                message = uiState.error,
                imageId = R.drawable.aa
            )

    }
}
