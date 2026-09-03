package com.example.totanpay.feature.balance
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.common.ui.ReadCardContent

@Composable
fun ReadCardScreen(
    viewModel: ReadCardViewModel,
    onGetTrack2: (String) -> Unit,
    onBackButtonClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context= LocalContext.current
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(uiState.track2) {
        if (uiState.track2.isNotEmpty()) {
            onGetTrack2(uiState.track2)
        }
    }
    LaunchedEffect(uiState.isTimeOut) {
        if (uiState.isTimeOut) onBackButtonClicked()
    }
    ReadCardContent(
        uiState,
        showFee = true,
        amountValue=null,
        amountTitle=null,
        onBackButtonClicked = { onBackButtonClicked() },
        readCard = {
            viewModel.readCard(context = context)
        },
        hideInternetErrorMessage = {
            viewModel.hideInternetErrorMessage()
        })
}


