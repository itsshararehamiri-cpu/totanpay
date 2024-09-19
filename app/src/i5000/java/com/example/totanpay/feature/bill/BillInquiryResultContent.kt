package com.example.totanpay.feature.bill


import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.theme.MARGIN_TOP_Bill_INQUERY_ROW

@Composable
fun BillInquiryResultContent(
    amount: String, billId: String,
    paymentId: String,
    serviceDesc: String?,
    billType: String?,
    onPayment: () -> Unit,
    onBackClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(
        Modifier
            .wrapContentHeight()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.Enter) {
                        onPayment()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            .verticalScroll(rememberScrollState())) {
        ConstraintLayout(
            ConstraintSet {
                val billProperty = createRefFor("billProperty")
                val amountOfBill = createRefFor("amount")
                constrain(billProperty) {
                    top.linkTo(parent.top,10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(amountOfBill) {
                    top.linkTo(billProperty.bottom)
                    end.linkTo(billProperty.end)
                    start.linkTo(billProperty.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Box(
                modifier = Modifier
                    .layoutId("billProperty")
                    .padding(horizontal = 12.dp)
                    .padding(top = 30.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 30.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    com.example.totanpay.common.receipt.RowReceipt(
                        modifier = Modifier.padding(top = MARGIN_TOP_Bill_INQUERY_ROW),
                        first = stringResource(id = R.string.bill_id),
                        second = billId,
                        textColor = MaterialTheme.colorScheme.onSurface, isPaperReceipt = false
                    )
                    com.example.totanpay.common.receipt.RowReceipt(
                        modifier = Modifier.padding(top = MARGIN_TOP_Bill_INQUERY_ROW),
                        first = stringResource(id = R.string.payment_id),
                        second = paymentId,
                        textColor = MaterialTheme.colorScheme.onSurface, isPaperReceipt = false
                    )
                    if (!serviceDesc.isNullOrEmpty())
                        com.example.totanpay.common.receipt.RowReceipt(
                            modifier = Modifier.padding(top = MARGIN_TOP_Bill_INQUERY_ROW),
                            first = stringResource(id = R.string.service_desc),
                            second = serviceDesc,
                            textColor = MaterialTheme.colorScheme.onSurface, isPaperReceipt = false
                        )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .layoutId("amount"),
            ) {
                Text(
                    text = stringResource(id = R.string.amount),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 2.dp)
                        .layoutId("first"),
                    color = MaterialTheme.colorScheme.onBackground,
                    style =
                    MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${amount.formatAmount()} ریال",
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 2.dp)
                        .layoutId("second"),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )
            }


        }
    }
}

