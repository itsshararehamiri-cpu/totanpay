package com.example.totanpay.feature.voucher

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.getAllCharges
import com.example.totanpay.ui.ListModifier
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.button.OperatorButton
import com.example.totanpay.ui.component.button.PriceButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING
import com.google.gson.Gson

@Composable
fun MainVoucherScreen(onConfirm: (String, String) -> Unit, onBackButtonClicked: () -> Unit) {
    var showToast by remember { mutableStateOf(false) }
    var operatorHasError: Boolean by remember { mutableStateOf(true) }
    var selectedOption by remember {
        mutableStateOf("")
    }
    var amountValue by remember {
        mutableStateOf("")
    }
    var amountHasError: Boolean by remember { mutableStateOf(false) }

    var selectedOperator: Operator? by remember {
        mutableStateOf(null)
    }
    var prices: List<String> by remember {
        mutableStateOf(listOf())
    }
    BackHandler {
        onBackButtonClicked()
    }
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    constraintSet = ConstraintSet {
                        val toolBar = createRefFor("toolBar")
                        val operatorTitle = createRefFor("operatorTitle")
                        val operators = createRefFor("operators")
                        val amountTitle = createRefFor("amountTitle")
                        val amounts = createRefFor("amounts")
                        val confirm = createRefFor("confirm")
                        constrain(toolBar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        constrain(operatorTitle) {
                            top.linkTo(toolBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        constrain(operators) {
                            top.linkTo(operatorTitle.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        constrain(amountTitle) {
                            top.linkTo(operators.bottom, 14.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        constrain(amounts) {
                            top.linkTo(amountTitle.bottom, 6.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        constrain(confirm) {
                            top.linkTo(amounts.bottom,16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    }
                ) {
                    BackButton(
                        title = stringResource(TransactionType.VOUCHER.title), modifier = BackButtonModifier
                            .layoutId("toolBar")
                    ) {
                        onBackButtonClicked()
                    }
                    Text(
                        text = stringResource(R.string.plz_choose_your_operator),
                        modifier = Modifier
                            .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                            .fillMaxWidth()
                            .layoutId("operatorTitle"),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val operators: List<Operator>? = getAllCharges(context = LocalContext.current)
                    LazyRow(
                        modifier = ListModifier
                            .layoutId("operators")
                    ) {
                        items(operators!!) { operator ->
                            OperatorButton(isSmall = true, isSelected = selectedOperator == operator,
                                operator,
                                modifier = Modifier.height(40.dp), onClick = {
                                    selectedOperator = it
                                    if (selectedOperator != null)
                                        prices = selectedOperator!!.chargeList
                                })
                        }
                    }
                    if (selectedOperator != null) {
                        Text(
                            modifier =   Modifier
                                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                                .fillMaxWidth().layoutId("amountTitle"),
                            text = stringResource(R.string.select_amount_amount),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = ListModifier
                                .layoutId("amounts")
                        ) {
                            items(prices.size) { item ->
                                PriceButton(
                                    selected = (selectedOption == prices[item]),
                                    title = prices[item],
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth(1f)
                                ) {
                                    selectedOption = it
                                    amountValue = it
                                }
                            }
                        }
                    }
                    MainButton(
                        modifier = Modifier.
                        mainButtonModifier(isSmall = isSmall(context = LocalContext.current) )
                            .layoutId("confirm")
                    ) {
                        if (amountValue.isNotEmpty() && selectedOperator != null) {
                            onConfirm(
                                amountValue,
                                Gson()
                                    .toJson(selectedOperator)
                                    .toString()
                            )
                        } else {
                            if (amountValue.isEmpty())
                                amountHasError = true
                            showToast = true
                        }
                    }
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = if (operatorHasError)
                    stringResource(R.string.operator_dont_select)
                else if (amountHasError) stringResource(R.string.amount_dont_set)
                else stringResource(R.string.values_dont_set)
            ) {
                showToast = false
            }
        }
    }
}
