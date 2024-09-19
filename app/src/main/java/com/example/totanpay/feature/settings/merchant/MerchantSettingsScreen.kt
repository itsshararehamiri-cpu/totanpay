package com.example.totanpay.feature.settings.merchant

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MerchantSettingsScreen(
    viewModel: MerchantSettingsViewModel,
    onBackClicked: () -> Unit,
    onA: () -> Unit,
    onPrinterSettings: () -> Unit,
    onChangeMerchantPassword: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackClicked()
    }
    MerchantSettingsContent(uiState,
        onBackClicked = { onBackClicked() },
        onA = { onA() },
        onPrinterSettings = { onPrinterSettings() },
        onChangeMerchantPassword = onChangeMerchantPassword,
        onEnableAndDisableVoice = {
            viewModel.changePlaySoundStatus(it)
        },
        onSelectTheme = {
            viewModel.changeTheme(it)
        }
    )
}

