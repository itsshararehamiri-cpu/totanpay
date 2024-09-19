package com.example.totanpay.feature.purchase

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.common.ui.ReadCardContent
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.balance.ReadCardViewModel

@Composable
fun ReadCardScreen(
    viewModel: ReadCardViewModel,
    amount: String,
    type: TransactionType,
    operator: String?,
    onGetTrack2: (String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.getExtraMessage(type,amount, operator,context)
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
        uiState, amount,
        amountTitle = when (type) {
            TransactionType.TOPUP -> "مبلغ شارژ:"
            TransactionType.PURCHASE -> "مبلغ خرید:"
            TransactionType.BILL_PAY -> "مبلغ قبض:"
            else -> ""
        },
        extraMessageValue =uiState.extraMessageValue,
        onBackButtonClicked = { onBackButtonClicked() },
        readCard = {
            viewModel.readCard()
        },
        hideInternetErrorMessage = { viewModel.hideInternetErrorMessage() }
    )
}


