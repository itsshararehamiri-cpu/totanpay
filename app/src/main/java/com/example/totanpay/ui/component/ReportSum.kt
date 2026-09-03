package com.example.totanpay.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddDateTime
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddNumberOfAllTransactions
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddSumOfAllTransactions
import com.example.totanpay.common.receipt.getFontSize
import com.example.totanpay.common.receipt.getFontWeight
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.theme.Dimensions.PADDING_SIDE_ROW_RECEIPT

@Composable
fun ReportSum(
    merchantName: String, merchantPhone: String,englishMerchantName: String,
    fromDate: String, fromTime: String,
    toDate: String, toTime: String, numberOfAllTransactions: String,
    sumOfAllTransactions: String
) {
    val context = LocalContext.current

        Column(
            modifier = Modifier
                .containerReceiptModifier(true, context)
        ) {
            val isPaperReceipt = true
            val firstColor = Color.Black
            val modifierRowReceipt = Modifier.rowReceiptModifier(true)
            if (!isPaperReceipt) {
                AddPSPLog(
                    modifier = Modifier.fillMaxWidth(),
                    color = firstColor,
                    isPaperReceipt = isPaperReceipt
                )
            }
            Row(
                modifier = modifierRowReceipt
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.general_sum),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
                    color = firstColor,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontSize = getFontSize(isPaperReceipt, context),
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                        ),
                    textAlign = TextAlign.Center
                )

            }
            AddMerchantNamePhone(
                modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
                merchantName = merchantName,
                merchantPhone = merchantPhone,
                englishMerchantName = englishMerchantName,
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            AddDateTime(
                modifier = modifierRowReceipt,
                date = "${stringResource(R.string.from_date)} $fromDate",
                time = "${stringResource(R.string.from_time)} $fromTime",
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            AddDateTime(
                modifier = modifierRowReceipt,
                date = "${stringResource(R.string.to_date)} $toDate",
                time = "${stringResource(R.string.to_time)} $toTime",
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            HorizontalDivider(
                modifierRowReceipt.padding(top = 3.dp),
                isPaperReceipt = isPaperReceipt
            )
            AddNumberOfAllTransactions(
                modifierRowReceipt.padding(top = 3.dp), number = numberOfAllTransactions,
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            AddSumOfAllTransactions(
                modifierRowReceipt,
                sum = sumOfAllTransactions,
                textColor = firstColor,
                isPaperReceipt = isPaperReceipt
            )
            if (isPaperReceipt) {
                AddPSPLog(
                    modifier = Modifier.fillMaxWidth(),
                    color = firstColor,
                    isPaperReceipt = isPaperReceipt
                )
            }
        }
}
