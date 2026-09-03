package com.example.totanpay.feature.bill

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BillInquiryResultScreen(
    viewModel: BillInquiryResultViewModel,
    billInquiryResult: String,
    billId: String,
    paymentId: String,
    onPayment: (String, String, String) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(billInquiryResult, billId, paymentId)
    }
    if (uiState.result != null)
        BillInquiryResultContent(
            amount = uiState.result!!.amount ?: "",
            billId = billId,
            paymentId = paymentId,
            serviceDesc = uiState.result!!.serviceDesc,
            englishServiceDesc = uiState.result!!.englishServiceDesc,
            onPayment = {
                onPayment(billId, paymentId, uiState.result!!.amount ?: "")
            },
            onBackClicked = onBackClicked
        )
}