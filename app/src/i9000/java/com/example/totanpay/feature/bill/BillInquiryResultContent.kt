package com.example.totanpay.feature.bill


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.Dimensions
import com.example.totanpay.ui.theme.MARGIN_TOP_Bill_INQUERY_ROW

@Composable
fun BillInquiryResultContent(
    amount: String, billId: String,
    paymentId: String,
    serviceDesc: String,
    englishServiceDesc: String,
    onPayment: () -> Unit,
    onBackClicked: () -> Unit
) {
    val isFarsi= LocalLanguageState.current.isFarsiSelected.value
    Box(
        Modifier
            .fillMaxSize()
           ) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val billProperty = createRefFor("billProperty")
                val amountOfBill = createRefFor("amount")
                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(billProperty) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(amountOfBill) {
                    top.linkTo(billProperty.bottom)
                    end.linkTo(billProperty.end)
                    start.linkTo(billProperty.start)
                    width = Dimension.fillToConstraints
                }

                constrain(confirm) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }

            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.bill_inquery), modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
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
                    if ((!serviceDesc.isNullOrEmpty() && isFarsi) || (!englishServiceDesc.isNullOrEmpty() && !isFarsi))
                        com.example.totanpay.common.receipt.RowReceipt(
                            modifier = Modifier.padding(top = MARGIN_TOP_Bill_INQUERY_ROW),
                            first = stringResource(id = R.string.service_desc),
                            second =if(isFarsi) serviceDesc else englishServiceDesc,
                            textColor = MaterialTheme.colorScheme.onSurface, isPaperReceipt = false
                        )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = MARGIN_TOP_Bill_INQUERY_ROW)
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
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize =
                        Dimensions.FONT_SIZE_RECEIPT, fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.amount_with_currency,
                        amount.formatAmount()),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 2.dp)
                        .layoutId("second"),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize =
                        Dimensions.FONT_SIZE_RECEIPT, fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.End
                )
            }


            MainButton(
                modifier =
                Modifier.mainButtonModifier(isSmall = false)
                    .layoutId("confirm")
            ) {
                onPayment()
            }

        }
    }
}

