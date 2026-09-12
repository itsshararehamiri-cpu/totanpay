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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.example.totanpay.R
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.Operator
import com.example.totanpay.ui.ListModifier
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.button.PriceButton
import com.google.gson.Gson

@Composable
fun AmountScreen(
    operator: String,
    isTopUp: Boolean,
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
                .verticalScroll(rememberScrollState())
                .padding(top = 40.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isTopUp) {
                PriceTextInput(
                    modifier = TextInputModifier,
                    title = stringResource(R.string.enter_amount_or_select),
                    errorMessage = amountError,
                    trailerTitle = stringResource(id = R.string.currency),
                    value = amountValue, hasError = amountHasError, onNextClicked = {
                        amountHasError = false
                        amountError = ""
                        showToast = false
                        if (amountValue.isEmpty()) {
                            showToast = true
                            amountHasError = true
                            amountError = context.getString(R.string.amount_not_entered)
                        } else {
                            onConfirm(amountValue)
                        }
                    }
                ) {
                    amountValue = it
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = ListModifier
                    .padding(top = if (isTopUp) 6.dp else 0.dp)
            ) {
                prices.forEach { price ->
                    PriceButton(
                        selected = (selectedOption == price),
                        title = price,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        amountHasError = false
                        amountError = ""
                        selectedOption = it
                        amountValue = it
                    }
                }
            }
            if (!isTopUp) {
                MainButton(
                    modifier = Modifier
                        .mainButtonModifier(isSmall = true)
                        .padding(top = 16.dp)
                ) {
                    if (amountValue.isNotEmpty()) {
                        onConfirm(amountValue)
                    } else {
                        amountHasError = true
                        showToast = true
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


