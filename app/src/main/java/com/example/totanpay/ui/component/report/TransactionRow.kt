package com.example.totanpay.ui.component.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.common.receipt.getFontSize
import com.example.totanpay.common.receipt.getFontWeight
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun TransactionRow(
    transaction: ResponseTransaction, index: Int, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context= LocalContext.current
    ConstraintLayout(
        ConstraintSet {
            val row1 = createRefFor("row1")
            val time = createRefFor("time")
            val trace = createRefFor("trace")
            val amount = createRefFor("amount")
            val type = createRefFor("type")
            val divider = createRefFor("divider")
            constrain(row1) {
                top.linkTo(parent.top, 5.dp)
                start.linkTo(parent.start)
                width = Dimension.percent(0.15f)
            }
            constrain(time) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                start.linkTo(row1.end, 5.dp)
                width = Dimension.percent(0.15f)
            }
            constrain(trace) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                start.linkTo(time.end, 5.dp)
                width = Dimension.percent(0.2f)

            }
            constrain(type) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                end.linkTo(parent.end)
                width = Dimension.percent(0.25f)
            }
            constrain(amount) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                end.linkTo(type.start, 5.dp)
                width = Dimension.percent(0.25f)
            }
            constrain(divider) {
                top.linkTo(row1.bottom, 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        }, modifier = Modifier
            .padding(horizontal = if (isPaperReceipt) 0.dp else 5.dp)
            .fillMaxWidth(1f)
    ) {

        Text(
            text = context.getString(transaction.transactionType),
            modifier = Modifier.layoutId("type"),
            color = textColor, style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )

        Text(
            text = transaction.amount.formatAmount(), modifier = Modifier.layoutId("amount"),
            color = textColor, style = MaterialTheme.typography.titleSmall
                .copy(
                    fontSize =  getFontSize(isPaperReceipt,context),
                    fontWeight = getFontWeight(isPaperReceipt,context)
                ), textAlign = TextAlign.Center
        )

        Text(
            text = transaction.trace,
            modifier = Modifier.layoutId("trace"),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )
        Text(
            text = transaction.time,
            modifier = Modifier.layoutId("time"),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )

        Text(
            text = index.toString(), modifier = Modifier
                .padding(start = 5.dp)
                .layoutId("row1"),
            color = textColor, style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(textColor)
                .layoutId("divider")
        )
    }
}

