package com.example.totanpay.feature.settings.merchant

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ApportionmentScreen(
    viewModel: ApportionmentViewModel,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.isFinish) {
        if (uiState.isFinish) {
            onBackClicked()
        }
    }
    BackHandler {
        onBackClicked()
    }
    ApportionmentContent(uiState = uiState,
        confirmApportionment = {
            viewModel.confirmApportionment() },
        addApportionment = { apportionment, amount ->
            viewModel.addApportionment(
                apportionment,
                amount
            )
        },
        onBackClicked = {
            onBackClicked()
        }
    )
}

