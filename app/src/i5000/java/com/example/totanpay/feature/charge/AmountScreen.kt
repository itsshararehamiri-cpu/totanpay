package com.example.totanpay.feature.charge


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
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
import com.example.totanpay.data.Operator
import com.example.totanpay.ui.ListModifier
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.PriceButton
import com.google.gson.Gson

@Composable
fun AmountScreen(
    operator: String,
    onConfirm: (amount: String) -> Unit,
    onBackButton: () -> Unit
) {
    val context = LocalContext.current
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf("") }
    var amountValue: String by remember { mutableStateOf("") }
    var selectedOption by remember {
        mutableStateOf("")
    }
    var showToast by remember { mutableStateOf(false) }
    val selectedOperator: Operator? = remember(operator) {
        try {
            Gson().fromJson(operator, Operator::class.java)
        } catch (e: Exception) {
            null
        }
    }
    val prices: List<String> = selectedOperator?.chargeList ?: listOf()
    BackHandler {
        onBackButton()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background),
                constraintSet = ConstraintSet {
                    val amountInput = createRefFor("amountInput")
                    val amounts = createRefFor("amounts")

                    constrain(amountInput) {
                        top.linkTo(parent.top,40.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(amounts) {
                        top.linkTo(amountInput.bottom, 6.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                }
            ) {

                PriceTextInput(
                    modifier = TextInputModifier
                        .layoutId("amountInput"),
                    title = stringResource(R.string.enter_amount_or_select),
                    errorMessage = amountError,
                    trailerTitle = stringResource(id = R.string.currency),
                    value = amountValue, hasError = amountHasError, onNextClicked = {
                        amountHasError = false
                        amountError = ""
                        showToast = false
                        amountError = ""
                        if (amountValue.isEmpty()) {
                            showToast=true
                            amountHasError = true
                            amountError = context.getString(R.string.amount_not_entered)
                        } else {
                            onConfirm(amountValue)
                        }
                    }
                ) {
                    amountValue = it
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = ListModifier
                        .layoutId("amounts")
                        .padding(horizontal = 4.dp)
                ) {
                    items(prices.size) { item ->
                        PriceButton(
                            selected = (selectedOption == prices[item]),
                            title = prices[item],
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            selectedOption = it
                            amountValue = it
                        }
                    }
                }


            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = if (amountHasError) {
                    stringResource(R.string.amount_not_entered)
                } else stringResource(R.string.values_are_not_entered)
            ) {
                showToast = false
            }
        }
    }
}



