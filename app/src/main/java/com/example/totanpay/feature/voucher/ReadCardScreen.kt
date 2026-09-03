package com.example.totanpay.feature.voucher

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.ui.ReadCardContent
//import com.example.totanpay.common.ui.ReadCardContent
import com.example.totanpay.feature.balance.ReadCardViewModel

@Composable
fun ReadCardScreen(
    viewModel: ReadCardViewModel,
    amount: String? = null,
    onGetTrack2: (String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val context= LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
    ReadCardContent(uiState,
        amountValue =amount,
        amountTitle = stringResource(R.string.amount_of_charge),
        onBackButtonClicked = { onBackButtonClicked() },
        hideInternetErrorMessage = {viewModel.hideInternetErrorMessage()},
        readCard = {
        viewModel.readCard(context)
    })
}