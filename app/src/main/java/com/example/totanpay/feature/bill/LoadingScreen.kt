package com.example.totanpay.feature.bill

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.component.Loading

@Composable
fun LoadingScreen(
    viewModel: BillInquiryViewModel,
    billId: String,
    paymentId: String,
    onSuccessResult: (String) -> Unit,
    onErrorResult: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        viewModel.inquiry(billId = billId, paymentId = paymentId)
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
    Loading()
}
