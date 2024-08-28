package com.example.totanpay.feature.reports

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.feature.purchase.PurchaseSuccessReceipt
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.theme.Background

@Composable
fun LastTransactionReportScreen(viewModel: LastTransactionViewModel, onBackButtonClicked:()->Unit){
    BackHandler {
        onBackButtonClicked()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (uiState.result != null)
            PurchaseSuccessReceipt(isPaperReceipt = false, uiState.result)
        MainButton(
            title = "چاپ رسید مشتری", modifier = Modifier
                .padding(top = 18.dp)
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()

        ) {
            println("yyyyyyyyyyyyyyyy")

        }
    }
}