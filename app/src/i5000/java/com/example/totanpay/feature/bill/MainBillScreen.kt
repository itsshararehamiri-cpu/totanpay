package com.example.totanpay.feature.bill

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun MainBillScreen(
    viewModel: MainBillViewModel,
    onConfirmBillAndPaymentId: (String, String) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var billIdValue: String by remember { mutableStateOf("") }
    var payIdValue: String by remember { mutableStateOf("") }

    var billIdError by remember { mutableStateOf("") }
    var payIdError by remember { mutableStateOf("") }

    var billIdHasError: Boolean by remember { mutableStateOf(false) }
    var payIdHasError: Boolean by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf("") }
    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(uiState) {
        if (uiState.barcode.isNotEmpty()) {
            billIdValue = uiState.barcode
        }
    }
    val focusManager = LocalFocusManager.current
    val billIdFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        billIdFocusRequester.requestFocus()
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val scan = createRefFor("scan")
                val title = createRefFor("title")
                val fieldsRow = createRefFor("fieldsRow")

                constrain(scan) {
                    top.linkTo(parent.top, 6.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(title) {
                    top.linkTo(scan.bottom, 4.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(fieldsRow) {
                    top.linkTo(title.bottom, 4.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Box(modifier = Modifier
                .padding(start = 18.dp)
                .layoutId("scan")
                .clickable {
                    viewModel.scan(context)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.scan__2_),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
                )
                Image(
                    painter = painterResource(id = R.drawable.scan__1_),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .layoutId("title"),
                text = stringResource(id = R.string.enter_bill_and_payment_id),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("fieldsRow")
            ) {
                TextInput(
                    modifier = Modifier
                        .padding(start = END_PADDING, end = START_PADDING)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .focusRequester(billIdFocusRequester),
                    hasError = billIdHasError, errorMessage = billIdError,
                    title = "",
                    placeholder = stringResource(id = R.string.bill_id),
                    value = billIdValue, onValueChange = {
                        if (!it.trim().isNotNumber() && it.length <= 13)
                            billIdValue = it
                    }, isSmall = true,
                    onNextClicked = {
                        focusManager.moveFocus(FocusDirection.Next)
                    })
                TextInput(
                    modifier = Modifier
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    hasError = payIdHasError, errorMessage = payIdError,
                    title = "",
                    placeholder = stringResource(id = R.string.payment_id),
                    value = payIdValue, onValueChange = {
                        if (!it.trim().isNotNumber() && it.length <= 13)
                            payIdValue = it
                    }, isSmall = true, onNextClicked = {
                        billIdHasError = false
                        payIdHasError = false
                        billIdError = ""
                        payIdError = ""
                        if (billIdValue.isNotEmpty() && payIdValue.isNotEmpty()) {
                            if (billIdValue.length in 6..13 && payIdValue.length in 6..13)
                                onConfirmBillAndPaymentId(billIdValue, payIdValue)
                            else {
                                if (billIdValue.length !in 6..13) {
                                    showErrorMessage = context.getString(R.string.bill_id_is_not_valid)
                                    billIdError = context.getString(R.string.bill_id_is_not_valid)
                                }
                                if (payIdValue.length !in 6..13) {
                                    showErrorMessage = context.getString(R.string.pay_id_is_not_valid)
                                    payIdError = context.getString(R.string.pay_id_is_not_valid)
                                }
                            }
                        } else {
                            if (billIdValue.isEmpty()) {
                                billIdHasError = true
                                billIdError = context.getString(R.string.bill_id_is_not_entered)
                            }
                            if (payIdValue.isEmpty()) {
                                payIdHasError = true
                                payIdError = context.getString(R.string.pay_id_is_not_entered)
                            }
                            showToast = true
                        }

                    })
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

