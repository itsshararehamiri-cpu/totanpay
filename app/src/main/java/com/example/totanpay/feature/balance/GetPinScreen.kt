package com.example.totanpay.feature.balance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.PlaybackSoundEffect

@Composable
fun GetPinScreen(
    track2: String,
    viewModel: GetPinViewModel,
    onGoLoading: (String) -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context=LocalContext.current
    BackHandler {
        onCancel()
    }
    PlaybackSoundEffect(uiState.playbackSound,R.raw.plz_enter_pin)
    LaunchedEffect(Unit) {
        viewModel.getPin(track2, context = context)
    }
    LaunchedEffect(uiState.getPin) {
        if (uiState.getPin)
            onGoLoading(uiState.pinBlock)
    }
    LaunchedEffect(uiState.isCancel) {
        if (uiState.isCancel)
            onCancel()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
    }


}