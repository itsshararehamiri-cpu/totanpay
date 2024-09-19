package com.example.totanpay.ui.component.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.common.receipt.getFontSize
import com.example.totanpay.common.receipt.getFontWeight
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun HeaderRow(
    modifier: Modifier,
    titles: List<String>,
    textColor: androidx.compose.ui.graphics.Color,
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
                top.linkTo(parent.top, if (isPaperReceipt) 2.dp else 5.dp)
                start.linkTo(parent.start)
                width = Dimension.percent(0.15f)
            }
            constrain(time) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                start.linkTo(row1.end, if (isPaperReceipt) 2.dp else 5.dp)
                width = Dimension.percent(0.15f)
            }
            constrain(trace) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                start.linkTo(time.end, if (isPaperReceipt) 2.dp else 5.dp)
                width = Dimension.percent(0.2f)
            }
            constrain(type) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                end.linkTo(parent.end)
                start.linkTo(amount.end)
                width = Dimension.percent(0.24f)
            }
            constrain(amount) {
                top.linkTo(row1.top)
                bottom.linkTo(row1.bottom)
                start.linkTo(trace.end)
                end.linkTo(type.start, if (isPaperReceipt) 2.dp else 5.dp)
                width = Dimension.percent(0.26f)
            }
            constrain(divider) {
                top.linkTo(row1.bottom, if (isPaperReceipt) 2.dp else 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        }, modifier = modifier
            .padding(horizontal = if (isPaperReceipt) 0.dp else 12.dp)
            .fillMaxWidth(if (isPaperReceipt) 0.5f else 1f)
            .border(
                1.dp,
                if (isPaperReceipt) Black else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                )
            )
            .background(
                color = if (isPaperReceipt) White else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
    ) {

        Text(
            text = titles[0],
            modifier = Modifier
                .padding(end = 5.dp)
                .layoutId("type")
                .padding(top = 5.dp, bottom = 5.dp),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = titles[1],
            modifier = Modifier
                .layoutId("amount")
                .padding(top = 5.dp, bottom = 5.dp),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context),
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = titles[2],
            modifier = Modifier
                .layoutId("trace")
                .padding(top = 5.dp, bottom = 5.dp),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context),
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = titles[3],
            modifier = Modifier
                .layoutId("time")
                .padding(top = 5.dp, bottom = 5.dp),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )

        Text(
            text = titles[4],
            modifier = Modifier
                .padding(start = 5.dp)
                .layoutId("row1")
                .padding(top = 5.dp, bottom = 5.dp),
            color = textColor,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize =  getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ), textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun HeaderRowPreview(){
    TotanPayTheme {
        Column(Modifier.fillMaxHeight()) {
            HeaderRow(
                modifier = Modifier.fillMaxWidth(1f), listOf(
                    stringResource(id = R.string.transaction_type),
                    stringResource(id = R.string.amount),
                    stringResource(id = R.string.trace),
                    stringResource(id = R.string.time),
                    stringResource(id = R.string.row1)
                ), textColor = Black, isPaperReceipt = true
            )
        }
    }
}