package com.example.totanpay.feature.bill

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton

@Composable
fun MainBillScreen(
    viewModel: MainBillViewModel,
    onConfirmBillAndPaymentId: (String, String) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var billIdValue: String by remember { mutableStateOf("") }
    var payIdValue: String by remember { mutableStateOf("") }

    var billIdHasError: Boolean by remember { mutableStateOf(false) }
    var payIdHasError: Boolean by remember { mutableStateOf(false) }

    var billIdError by remember { mutableStateOf("") }
    var payIdError by remember { mutableStateOf("") }

    var showToast by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf("") }
    LaunchedEffect(uiState) {
        if (uiState.barcode.isNotEmpty()) {
            if (uiState.barcode.length == 26) {
                billIdValue = uiState.barcode.substring(0, 13)
                payIdValue = uiState.barcode.substring(13, 26)
            }
        }
    }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val scan = createRefFor("scan")
                val billId = createRefFor("billId")
                val payId = createRefFor("payId")
                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(scan) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(billId) {
                    top.linkTo(scan.bottom)
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.bill_payment), modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            Box(modifier = Modifier
                .padding(start = 18.dp)
                .layoutId("scan")
                .clickable {
                    viewModel.scan(context)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.scan__2_),
                    contentDescription = "",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
                )
                Image(
                    painter = painterResource(id = R.drawable.scan__1_),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            TextInput(
                modifier = TextInputModifier
                    .layoutId("billId"),
                hasError = billIdHasError, errorMessage = billIdError,
                title = stringResource(id = R.string.bill_id),
                value = billIdValue, onValueChange = {
                    if (!it.trim().toEnglishNumber().isNotNumber() && it.length <= 13)
                    {
                        billIdValue = it.toEnglishNumber()
                    }
                }, isSmall = false,
                onNextClicked = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
            TextInput(
                modifier = TextInputModifier
                    .layoutId("payId"),
                hasError = payIdHasError, errorMessage = payIdError,
                title = stringResource(id = R.string.payment_id),
                value = payIdValue, onValueChange = {
                    if (!it.trim().toEnglishNumber().isNotNumber() && it.length <= 13)
                        payIdValue = it.toEnglishNumber()
                },  isSmall = false,onNextClicked = {
                    keyboard?.hide()
                })
            MainButton(
                modifier=Modifier.mainButtonModifier(isSmall = false)
                    .layoutId("confirm")
            ) {
                billIdHasError=false
                payIdHasError=false
                billIdError=""
                payIdError=""
                if (billIdValue.isNotEmpty() && payIdValue.isNotEmpty()) {
                    if (billIdValue.length in 6..13 && payIdValue.length in 6..13)
                        onConfirmBillAndPaymentId(billIdValue, payIdValue)
                    else {
                        if (billIdValue.length !in 6..13)
                        {
                            showErrorMessage = context.getString(R.string.bill_id_is_not_valid)
                            billIdError= context.getString(R.string.bill_id_is_not_valid)
                        }
                        if (payIdValue.length !in 6..13)
                        {
                            showErrorMessage = context.getString(R.string.pay_id_is_not_valid)
                            payIdError = context.getString(R.string.pay_id_is_not_valid)
                        }
                    }
                } else {
                    if (billIdValue.isEmpty()) {
                        billIdHasError = true
                        billIdError= context.getString(R.string.bill_id_is_not_entered)
                    }
                    if (payIdValue.isEmpty()) {
                        payIdHasError = true
                        payIdError= context.getString(R.string.pay_id_is_not_entered)
                    }
                    showToast = true
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = if (billIdHasError) context.getString(R.string.bill_id_is_not_entered)
                else if (payIdHasError) context.getString(R.string.pay_id_is_not_valid)
                else context.getString(R.string.values_are_not_entered)
            ) {
                showToast = false
            }
        }
        if (showErrorMessage.isNotEmpty()) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = showErrorMessage
            ) {
                showErrorMessage = ""
            }
        }

    }

}
