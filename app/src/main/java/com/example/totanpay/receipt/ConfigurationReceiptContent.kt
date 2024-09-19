package com.example.totanpay.receipt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.totanpay.R
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.RowReceipt
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.feature.settings.supervisor.ConfigurationResult
import com.example.totanpay.ui.theme.Black

@Composable
fun ConfigurationReceiptContent(configurationResult: ConfigurationResult) {
    val context= LocalContext.current

    Column(
        Modifier.containerReceiptModifier(true,context)
    ) {
        val modifier = Modifier.rowReceiptModifier(true)
        val textColor = Color.Black
        if (configurationResult.merchantName.isNotEmpty()) {
            RowReceipt(
                modifier = modifier,
                second = configurationResult.merchantPhone,
                first = configurationResult.merchantName,
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (configurationResult.terminalId.isNotEmpty())
        {
            RowReceipt(
                modifier = modifier,
                first = configurationResult.date,
                second = configurationResult.time,
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (configurationResult.terminalId.isNotEmpty())
        {
            RowReceipt(
                modifier = modifier,
                first = configurationResult.terminalId,
                second = configurationResult.merchantId,
                textColor = textColor, isPaperReceipt = true
            )
        }
        if (configurationResult.posCode.isNotEmpty())
        {
            RowReceipt(
                modifier = modifier,
                first = stringResource(R.string.pos_code),
                second = configurationResult.posCode,
                textColor = textColor, isPaperReceipt = true
            )
        }
        AddPSPLog(
            modifier = Modifier.fillMaxWidth(),
            color = Black,
            isPaperReceipt = true
        )
    }
}