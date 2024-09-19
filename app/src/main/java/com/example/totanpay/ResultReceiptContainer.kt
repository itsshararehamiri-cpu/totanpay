package com.example.totanpay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.common.ReceiptResultColumnModifier
import com.example.totanpay.common.ReceiptResultModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.resultLogoMoifier
import com.example.totanpay.ui.component.ResultLogo

@Composable
fun ResultReceiptContainer(modifier:Modifier,isPaperReceipt:Boolean,isSuccess:Boolean, content: @Composable () -> Unit){

    Box(
        modifier = modifier.ReceiptResultModifier(isPaperReceipt)
    )
    {
        Column(
            modifier = Modifier.ReceiptResultColumnModifier(isSmall= isSmall(LocalContext.current)), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultLogo(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .resultLogoMoifier(isSmall = isSmall(LocalContext.current))
                    .layoutId("successTickImage"), isSuccess = isSuccess
            )
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .verticalScroll(scrollState)
            ) {
                content()
            }
        }
    }

}