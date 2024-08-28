package com.example.totanpay.feature.bill

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.MARGIN_SIDE_MAIN_CONFIRM
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun MainBillScreen(viewModel: BillViewModel, onConfirmBillAndPayId: (String, String) -> Unit,
                   onBackClicked:()->Unit) {
    var billIdValue: String by remember { mutableStateOf("1233333") }
    var payIdValue: String by remember { mutableStateOf("1233333") }
    BackHandler {
        onBackClicked()
    }
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val billId = createRefFor("billId")
            val payId = createRefFor("payId")
            val confirm = createRefFor("confirm")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(billId) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(payId) {
                top.linkTo(billId.bottom)
                end.linkTo(billId.end)
                start.linkTo(billId.start)
                width = Dimension.fillToConstraints
            }
            constrain(confirm) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }

        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = "پرداخت قبض", modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ){
            onBackClicked()
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("billId"),
            title = "شناسه قبض",
            trailerTitle = "",
            value = billIdValue
        ) {
            billIdValue = it
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("payId"),
            title = "شناسه پرداخت",
            trailerTitle = "",
            value = payIdValue
        ) {
            payIdValue = it
        }
        MainButton(
            title = "تایید",
            modifier = Modifier
                .padding(
                    bottom = MARGIN_BOTTOM_MAIN_CONFIRM,
                    start = END_PADDING, end = START_PADDING, top = 16.dp
                )

                .layoutId("confirm")
                .padding(top = 18.dp)
                .fillMaxWidth()

        ){
            if (billIdValue.isNotEmpty() && payIdValue.isNotEmpty())
                viewModel.billInquery(billIdValue, payIdValue)
            //   onConfirmBillAndPayId(billIdValue, payIdValue)
        }
    }
}

@Composable
@Preview
fun MainBillPreview() {
    TotanPayTheme {
        MainBillScreen(
            hiltViewModel(),
            onConfirmBillAndPayId = { billId, payId -> }, onBackClicked = {}
        )
    }
}
