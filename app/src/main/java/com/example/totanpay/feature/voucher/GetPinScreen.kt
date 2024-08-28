package com.example.totanpay.feature.voucher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GetPinScreen(amount:String, track2:String,operator:String, viewModel: GetPinViewModel, onGoLoading:( String)->Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getPin(track2)
    }
    LaunchedEffect(uiState.getPin) {
        if(uiState.getPin)
            onGoLoading(uiState.pinBlock)
    }

}