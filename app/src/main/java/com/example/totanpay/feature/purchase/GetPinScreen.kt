package com.example.totanpay.feature.purchase

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.PlaybackSoundEffect

@Composable
fun GetPinScreen(
    amount: String,
    track2: String,
    viewModel: GetPinViewModel,
    onGoLoading: (amount: String, pinBlock: String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaybackSoundEffect(uiState.playbackSound,  R.raw.plz_enter_pin)
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.getPin(track2)
    }
    LaunchedEffect(uiState.getPin) {
        if (uiState.getPin)
            onGoLoading(amount, uiState.pinBlock)
    }
    LaunchedEffect(uiState.isCancel) {
        if (uiState.isCancel)
            onBackButtonClicked()
    }
}