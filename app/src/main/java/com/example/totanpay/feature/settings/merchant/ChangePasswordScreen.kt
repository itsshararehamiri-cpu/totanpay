package com.example.totanpay.feature.settings.merchant

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ChangeMerchantPasswordScreen(
    viewModel: ChangeMerchantPasswordViewModel,
    onBackClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(uiState.confirmSettings) {
        if (uiState.confirmSettings)
            onBackClicked()
    }
    ChangeMerchantPasswordContent(
        uiState
        = uiState, onBackClicked = onBackClicked
    ) {
        viewModel.setMerchantPassword(it)
    }
}


@Composable
@Preview
fun SleepingModeDeviceScreenPreview() {
    TotanPayTheme {
        // ChangeMerchantPasswordScreen(){}
    }
}