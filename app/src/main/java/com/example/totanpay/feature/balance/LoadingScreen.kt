package com.example.totanpay.feature.balance

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.component.Loading
@Composable
fun LoadingScreen(
    track2: String,
    pinBlock:String,
    viewModel: BalanceViewModel,
    onSuccessResult: (String) -> Unit,
    onErrorResult:(String)->Unit,
    onBackButtonClicked:()->Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.balance(track2 = track2, pinBlock = pinBlock)
    }
    LaunchedEffect(uiState.isSuccessful) {
        if (uiState.isSuccessful)
        {
            onSuccessResult(uiState.response)
        }
    }
    LaunchedEffect(uiState.isUnSuccessful) {
        if (uiState.isUnSuccessful)
        {
            onErrorResult(uiState.response)
        }
    }
    Loading()
}


