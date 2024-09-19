package com.example.totanpay.feature.topup

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.formatAmount
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.getAllCharges
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.ListModifier
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.MobileTextInput
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.button.OperatorButton
import com.example.totanpay.ui.component.button.PriceButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING
import com.google.gson.Gson

@Composable
fun MainTopUpScreen(
    onConfirm: (mobile: String, amount: String, operator: String) -> Unit, onBackButton: () -> Unit
) {
    var operatorHasError: Boolean by remember { mutableStateOf(true) }
    var mobileHasError: Boolean by remember { mutableStateOf(false) }
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var phoneNumberValue: String by remember { mutableStateOf("") }
    var amountValue: String by remember { mutableStateOf("") }
    var mobileError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var selectedOperator: Operator? by remember {
        mutableStateOf(null)
    }
    var selectedOption by remember {
        mutableStateOf("")
    }
    var showToast by remember { mutableStateOf(false) }
    var prices: List<String> by remember {
        mutableStateOf(listOf())
    }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    BackHandler {
        onBackButton()
    }
    Box(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val operatorTitle = createRefFor("operatorTitle")
                val phoneNumber = createRefFor("phoneNumber")
                val operators = createRefFor("operators")
                val amountInput = createRefFor("amountInput")
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
                constrain(phoneNumber) {
                    top.linkTo(operators.bottom, 14.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(amountInput) {
                    top.linkTo(phoneNumber.bottom, 14.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(amounts) {
                    top.linkTo(amountInput.bottom, 6.dp)
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = TransactionType.TOPUP.title,
                modifier = BackButtonModifier.layoutId("toolBar")
            ) {
                onBackButton()
            }
            Text(
                text = stringResource(R.string.plz_select_operator),
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                    .fillMaxWidth()
                    .layoutId("operatorTitle"),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            MobileTextInput(modifier = TextInputModifier.layoutId("phoneNumber"),
                errorMessage = mobileError,
                title = stringResource(R.string.enter_your_mobile),
                value = phoneNumberValue,
                hasError = mobileHasError,
                onValueChange = {
                    if (it.length <= 11 && !it.trim().isNotNumber()) {
                        phoneNumberValue = it
                    }
                },
                isSmall = false,
                onNextClicked = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
            val operators: List<Operator>? = getAllCharges(context = context)
            LazyRow(
                modifier = ListModifier.layoutId("operators")
            ) {
                items(operators!!) { operator ->
                    OperatorButton(
                        isSmall = false,
                        isSelected = selectedOperator == operator,
                        operator,
                        modifier = Modifier
                    ) {
                        selectedOperator = it
                        operatorHasError = false
                        if (selectedOperator != null) prices = selectedOperator!!.chargeList
                    }
                }
            }
            if (selectedOperator != null) {
                PriceTextInput(modifier = TextInputModifier.layoutId("amountInput"),
                    errorMessage = amountError,
                    title = stringResource(R.string.enter_amount_or_select),
                    trailerTitle = stringResource(id = R.string.currency),
                    value = amountValue,
                    hasError = amountHasError,
                    onNextClicked = {
                        keyboard?.hide()
                    }) {
                    amountError = ""
                    amountHasError = false
                    if (it.isNotEmpty()) {
                        if (it.toLong() <= 1000000)
                            amountValue = it
                        else {
                            amountError = context.getString(R.string.amount_can_not_be_greater_than,"1000000".formatAmount())
                            amountHasError = true
                        }
                    }
                    else{
                        amountValue=""
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = ListModifier.layoutId("amounts")
                ) {
                    items(prices.size) { item ->
                        PriceButton(
                            selected = (selectedOption == prices[item]),
                            title = prices[item],
                            modifier = Modifier.height(40.dp)
                        ) {
                            selectedOption = it
                            amountValue = it
                        }
                    }
                }
            }
            MainButton(
                modifier = Modifier
                    .mainButtonModifier(isSmall = false)
                    .layoutId("confirm")
            ) {
                if (phoneNumberValue.isNotEmpty() && amountValue.isNotEmpty() && selectedOperator != null) {
                    if (((selectedOperator!!.code == 12 || selectedOperator!!.code == 11) && amountValue.toLong() in 50000L..1000000L) ||
                        (selectedOperator!!.code == 17 && amountValue.toLong() in 10000L..1000000L)
                    ) {
                        onConfirm(
                            phoneNumberValue,
                            amountValue,
                            Gson().toJson(selectedOperator).toString()
                        )
                    } else {
                        amountHasError = true
                        amountError = context.getString(R.string.amount_is_not_valid)
                        showToast = true
                    }
                } else {
                    amountError = ""
                    mobileError = ""
                    if (amountValue.isEmpty()) {
                        amountHasError = true
                        amountError = context.getString(R.string.amount_not_entered)
                    }
                    if (phoneNumberValue.isEmpty()) {
                        mobileHasError = true
                        mobileError = context.getString(R.string.mobile_not_entered)

                    }
                    showToast = true
                }
            }

            if (showToast) {
                ShowToast(modifier = Modifier.align(Alignment.BottomCenter),
                    message = if (operatorHasError) {
                        stringResource(R.string.operator_not_selected)
                    } else if (mobileHasError) {
                        stringResource(R.string.mobile_not_entered)
                    } else if (amountHasError) {
                        amountError.ifEmpty { stringResource(R.string.amount_not_entered) }
                    } else stringResource(R.string.values_are_not_entered)) {
                    showToast = false
                }
            }
        }
    }
}