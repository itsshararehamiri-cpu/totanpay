package com.example.totanpay.feature.topup

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.dialog.ConnectionMessageDialog

@Composable
fun LoadingScreen(
    track2: String,
    amount: String,
    pinBlock: String,
    operator: String,
    mobile: String,
    viewModel: TopUpViewModel,
    onSuccessResult: (String) -> Unit,
    onErrorResult: (String) -> Unit,
    onBackButtonClicked:()->Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.topup(track2 = track2, amount = amount, pinBlock = pinBlock, operator = operator,
            mobile=mobile)
    }
    if (uiState.isSuccessful) {
    LaunchedEffect(lifecycle) {
            onSuccessResult(uiState.response)
        }
    }
    LaunchedEffect(uiState.isUnSuccessful) {
        if (uiState.isUnSuccessful) {
            onErrorResult(uiState.response)
        }
    }
    if (uiState.connectionError) {
        ConnectionMessageDialog {
            onBackButtonClicked()
        }
    }
    Loading()

}

