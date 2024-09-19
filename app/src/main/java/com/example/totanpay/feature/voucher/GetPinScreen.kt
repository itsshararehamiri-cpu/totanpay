package com.example.totanpay.feature.voucher

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
    operator: String,
    viewModel: GetPinViewModel,
    onGoLoading: (String) -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onCancel()
    }
    PlaybackSoundEffect(uiState.playbackSound,R.raw.plz_enter_pin)
    BackHandler {
        onCancel()
    }
    LaunchedEffect(Unit) {
        viewModel.getPin(track2)
    }
    LaunchedEffect(uiState.getPin) {
        if (uiState.getPin)
        {
            onGoLoading(uiState.pinBlock)
        }
    }

    LaunchedEffect(uiState.isCancel) {
        if (uiState.isCancel)
            onCancel()
    }
}