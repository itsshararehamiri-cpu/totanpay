package com.example.totanpay.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddDateTime
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddNumberOfAllTransactions
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddSumOfAllTransactions
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.util.formatAmount

@Composable
fun ReportSum(
    merchantName: String, merchantPhone: String,
    fromDate: String, fromTime: String,
    toDate: String, toTime: String, numberOfAllTransactions: String,
    sumOfAllTransactions: String
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
            AddMerchantNamePhone(
                modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
                merchantName = merchantName,
                merchantPhone = merchantPhone,
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            AddDateTime(
                modifier = modifierRowReceipt,
                date = "از تاریخ $fromDate",
                time = "از ساعت $fromTime",
                textColor = firstColor, isPaperReceipt = isPaperReceipt
            )
            AddDateTime(
                modifier = modifierRowReceipt,
                date = "تا تاریخ $toDate",
                time = "تا ساعت $toTime",
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
                sum = sumOfAllTransactions.formatAmount(),
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
}
