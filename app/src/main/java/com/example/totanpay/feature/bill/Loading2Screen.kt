package com.example.totanpay.feature.bill

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.component.Loading

@Composable
fun Loading2Screen(
    track2: String,
    amount: String,
    pinBlock: String,
    billId: String,
    paymentId: String,
    viewModel: BillViewModel2,
    onSuccessResult: (String) -> Unit,
    onErrorResult: (String) -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        viewModel.billPayment(billId=billId,paymentId=paymentId,amount=amount,track2=track2,pinBlock=pinBlock, serviceSesc = "")
    }
    if (uiState.isSuccessful) {
    LaunchedEffect(lifecycle) {

            onSuccessResult(uiState.response)
        }
    }
    if (uiState.isUnSuccessful) {
    LaunchedEffect(uiState.isUnSuccessful) {
            onErrorResult(uiState.response)
        }
    }
    Loading()
}