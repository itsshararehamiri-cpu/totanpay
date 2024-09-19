package com.example.totanpay.feature.reports

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet

@Composable
fun NotFoundResultScreen(onBackClicked: () -> Unit) {
    BackHandler {
        onBackClicked()
    }
    val scrollState= rememberScrollState()
    Box(
        modifier =  Modifier.fillMaxSize().verticalScroll(scrollState) ){
    ConstraintLayout(
        ConstraintSet {
            val notFoundTransaction = createRefFor("not_found_transaction")
            constrain(notFoundTransaction) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NotFoundTransaction(Modifier.layoutId("not_found_transaction")){
            onBackClicked()
        }

    }
}}