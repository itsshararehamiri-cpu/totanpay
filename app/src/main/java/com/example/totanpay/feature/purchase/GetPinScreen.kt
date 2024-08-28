package com.example.totanpay.feature.purchase

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GetPinScreen(amount:String,track2:String,viewModel: GetPinViewModel,onGoLoading:(amount:String,pinBlock:String)->Unit,
                 onBackButtonClicked:()->Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.getPin(track2)
    }
    LaunchedEffect(uiState.getPin) {
        if(uiState.getPin)
            onGoLoading(amount,uiState.pinBlock)
    }

}