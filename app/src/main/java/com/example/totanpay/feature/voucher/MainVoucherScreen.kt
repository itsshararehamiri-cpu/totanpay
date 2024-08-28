package com.example.totanpay.feature.voucher

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.data.OperatorContainer
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.component.OperatorButton
import com.example.totanpay.ui.component.PriceButton
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import com.google.gson.Gson

@Composable
fun MainVoucherScreen(onConfirm: (String, String) -> Unit, onBackButtonClicked: () -> Unit) {
    val prices = listOf("10000", "50000", "100000", "200000")
    var selectedOption by remember {
        mutableStateOf("")
    }
    var amountValue by remember {
        mutableStateOf("")
    }
    var selectedOperator: Operator? by remember {
        mutableStateOf(null)
    }
    BackHandler {
        onBackButtonClicked()
    }
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val operatorTitle = createRefFor("operatorTitle")
            val operators = createRefFor("operators")
            val amount = createRefFor("amount")
            val amounts = createRefFor("amounts")
            val confirm = createRefFor("confirm")

            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(operatorTitle) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(operators) {
                top.linkTo(operatorTitle.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(amount) {
                top.linkTo(operators.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(amounts) {
                top.linkTo(amount.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
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
            title = "خرید کد مستقیم", modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackButtonClicked()
        }
        Text(
            text = "لطفا اپراتور مورد نظر خود را انتخاب کنید", modifier = Modifier
                .fillMaxWidth()
                .layoutId("operatorTitle")
        )


        val operators = OperatorContainer.getOperators()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("operators")
        ) {
            items(operators) { operator ->
                OperatorButton(
                    operator,
                    modifier = Modifier.height(40.dp), onClick = {
                        selectedOperator=it
                    })
            }
        }
        TextInput(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)

                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("amount"),
            title = "مبلغ شارژ را انتخاب یا وارد کنید",
            trailerTitle = stringResource(id = R.string.currency),
            value = amountValue
        ) {
            amountValue = it
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(horizontal = END_PADDING)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .layoutId("amounts")
        ) {
            items(4) { item ->
                PriceButton(
                    selected = (selectedOption == prices.get(item)),
                    title = prices.get(item),
                    modifier = Modifier.height(40.dp)
                ) {
                    selectedOption = it
                    amountValue = it
                }
            }
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
            if (amountValue.isNotEmpty() && selectedOperator != null)
            {
                onConfirm(
                    amountValue,
                    Gson()
                        .toJson(selectedOperator)
                        .toString()
                )
            }
            else{
                Log.d("TAG", "MainVoucherScreen() called")
            }
        }
    }
}

@Composable
@Preview
fun MainBillPreview() {
    TotanPayTheme {
        MainVoucherScreen(
            onConfirm = { x, y -> }
        ) {}
    }
}

