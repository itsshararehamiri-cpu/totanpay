package com.example.totanpay.receipt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.R
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.RowReceipt
import com.example.totanpay.common.receipt.getFontSize
import com.example.totanpay.common.receipt.getFontWeight
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.feature.settings.supervisor.ConfigurationResult
import com.example.totanpay.ui.theme.Black
import com.example.totanpay.ui.theme.Dimensions.PADDING_SIDE_ROW_RECEIPT

@Composable
fun ConfigurationReceiptContent(configurationResult: ConfigurationResult) {
    val context= LocalContext.current
    val isFarsi= LocalLanguageState.current.isFarsiSelected.value
    Column(
        Modifier.containerReceiptModifier(true,context)
    ) {
        val modifier = Modifier.rowReceiptModifier(true)
        val textColor = Color.Black
        if (configurationResult.merchantName.isNotEmpty()) {
//            RowReceipt(
//                modifier = modifier,
//                second = configurationResult.merchantPhone,
//                first =if(isFarsi) configurationResult.merchantName else  configurationResult.englishMerchantName,
//                textColor = textColor, isPaperReceipt = true
//            )
            val isFarsi = LocalLanguageState.current.isFarsiSelected.value
//    RowReceipt(
//        modifier = modifier,
//        first = if (isFarsi) merchantName else englishMerchantName,
//        second = merchantPhone,
//        textColor = textColor, isPaperReceipt = isPaperReceipt
//    )
            val  isPaperReceipt=true
            val context = LocalContext.current
            val textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(true, context),
                fontWeight = getFontWeight(true, context)
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                // استفاده از Alignment.Top برای اینکه اگر یکی بلندتر شد، دیگری از بالا شروع شود
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = if(isFarsi) configurationResult.merchantName else  configurationResult.englishMerchantName,
                    modifier = Modifier
                        .weight(1f) // به این متن اجازه می‌دهیم فضا بگیرد و اگر طولانی شد، بشکند
                        .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
                    color = textColor,
                    style = textStyle.copy(lineHeight = (8 * 1.1).sp ,
                        platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                    ),

                    textAlign = TextAlign.End
                )

                // اگر می‌خواهید فاصله ثابتی بینشان باشد، یک Spacer کوچک بگذارید
                // Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text =configurationResult. merchantPhone,
                    modifier = Modifier
                        .weight(1f) // این هم به اندازه اولی فضا می‌گیرد
                        .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
                    color = textColor,
                    style = textStyle,
                    textAlign = TextAlign.Start
                )
            }
        }

        if (configurationResult.terminalId.isNotEmpty())
        {
            RowReceipt(
                modifier = modifier,
                first = stringResource(R.string.terminal),
                second = configurationResult.terminalId,
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