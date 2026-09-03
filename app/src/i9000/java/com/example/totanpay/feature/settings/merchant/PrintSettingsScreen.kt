package com.example.totanpay.feature.settings.merchant

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.MAXIMUM_AMOUNT_OF_TRANSACTION
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.repository.datasource.formatAmount
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.common.textFieldModifier
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.priceFilter
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun PrintSettingsScreen(viewModel: PrintingSettingsViewModel, onBackButtonClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    var amount: String by remember { mutableStateOf("") }
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    var showAmountIsNotCorrectRangeToast by remember { mutableStateOf(false) }

    var showErrorSelectOneOptionToast by remember { mutableStateOf(false) }

    var noPrintingValue: Boolean by remember { mutableStateOf(false) }
    var printReceiptValue: Boolean by remember { mutableStateOf(true) }
    var autoPrintReceiptValue: Boolean by remember { mutableStateOf(false) }

    var noPrintingMerchantValue: Boolean by remember { mutableStateOf(false) }
    var printingMerchantValue: Boolean by remember { mutableStateOf(true) }


    var showConfirmDialog: Boolean by remember { mutableStateOf(false) }

    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(uiState.printStatusForCustomer) {
        amount = uiState.minimumAmountForPrint
        when (uiState.printStatusForCustomer) {
            PrintStatus.NO_PRINTING -> {
                noPrintingValue = true
                printReceiptValue = false
                autoPrintReceiptValue = false
            }
            PrintStatus.PRINT -> {
                noPrintingValue = false
                printReceiptValue = true
                autoPrintReceiptValue = uiState.isAutoPrintCustomerReceipt
            }
            else -> {}
        }
    }
    LaunchedEffect(uiState.printStatusForMerchant) {
        when (uiState.printStatusForMerchant) {
            PrintStatus.NO_PRINTING -> {
                noPrintingMerchantValue = true
                printingMerchantValue = false
            }

            PrintStatus.PRINT -> {
                noPrintingMerchantValue = false
                printingMerchantValue = true
            }

            else -> {

            }
        }
    }
    LaunchedEffect(uiState.isAutoPrintCustomerReceipt) {
        autoPrintReceiptValue=uiState.isAutoPrintCustomerReceipt
    }
    LaunchedEffect(uiState.confirmSettings) {
        if (uiState.confirmSettings) onBackButtonClicked()
    }
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.Enter) {
                        showErrorSelectOneOptionToast = false
                        amountHasError = false
                        val customerOk = noPrintingValue || printReceiptValue
                        if (!customerOk || !(printingMerchantValue || noPrintingMerchantValue)) {
                            showErrorSelectOneOptionToast = true
                        } else {
                            showConfirmDialog = true
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val customerReceiptTitle = createRefFor("customerReceiptTitle")
                val noPrinting = createRefFor("noPrinting")
                val printReceipt = createRefFor("printReceipt")
                val autoPrintSwitchRow = createRefFor("autoPrintSwitchRow")
                val minAmountField = createRefFor("minAmountField")
                val merchantReceiptTitle = createRefFor("merchantReceiptTitle")
                val noPrintingMerchantReceipt = createRefFor("noPrintingMerchantReceipt")
                val printingMerchantReceipt = createRefFor("printingMerchantReceipt")

                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(customerReceiptTitle) {
                    top.linkTo(toolBar.bottom, 0.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(noPrinting) {
                    top.linkTo(customerReceiptTitle.bottom, 0.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(printReceipt) {
                    top.linkTo(noPrinting.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(autoPrintSwitchRow) {
                    top.linkTo(printReceipt.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(minAmountField) {
                    top.linkTo(autoPrintSwitchRow.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(merchantReceiptTitle) {
                    top.linkTo(minAmountField.bottom, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(noPrintingMerchantReceipt) {
                    top.linkTo(merchantReceiptTitle.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(printingMerchantReceipt) {
                    top.linkTo(noPrintingMerchantReceipt.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }

                constrain(confirm) {
                    top.linkTo(printingMerchantReceipt.bottom, 16.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            BackButton(
                title = stringResource(id = R.string.printer_settings),
                modifier = BackButtonModifier.layoutId("toolBar")
            ) {
                onBackButtonClicked()
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = END_PADDING)
                    .fillMaxWidth()
                    .layoutId("customerReceiptTitle"),
                text = stringResource(R.string.customer_receipt),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 3.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("noPrinting")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = noPrintingValue, onClick = {
                            amount = ""
                            noPrintingValue = !noPrintingValue
                            if (noPrintingValue) {
                                printReceiptValue = false
                                autoPrintReceiptValue = false
                            }
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.failur_to_print_receipt),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("printReceipt")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = printReceiptValue, onClick = {
                            printReceiptValue = !printReceiptValue
                            if (printReceiptValue) {
                                noPrintingValue = false
                            } else {
                                autoPrintReceiptValue = false
                                amount = ""
                            }
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.print_receipt),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                    .fillMaxWidth()
                    .layoutId("autoPrintSwitchRow")
            ) {
                if (printReceiptValue) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                                    )
                                ), shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.print_receipt_above_min_amount),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = autoPrintReceiptValue,
                            onCheckedChange = {
                                autoPrintReceiptValue = it
                                if (!it) amount = ""
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = Green50)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                    .fillMaxWidth()
                    .layoutId("minAmountField")
            ) {
                if (printReceiptValue && autoPrintReceiptValue) {
                    val minAmountLabel = stringResource(R.string.min_amount_for_auto_print_receipt)
                    val textFieldValue = TextFieldValue(text = amount, selection = TextRange(amount.length))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp).padding( bottom = 4.dp),
                            text = minAmountLabel,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp, brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                                        )
                                    ), shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .weight(1f)
                                        .textFieldModifier(isSmall = false)
                                        .background(MaterialTheme.colorScheme.surface),
                                    value = textFieldValue,
                                    onValueChange = {
                                        if (!it.text.trim().toEnglishNumber().isNotNumber())
                                            amount = it.text.trim().toEnglishNumber()
                                    },
                                    colors = TextFieldDefaults.colors()
                                        .copy(
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedIndicatorColor = Transparent,
                                            disabledContainerColor = MaterialTheme.colorScheme.surface
                                        ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        textDirection = TextDirection.Ltr,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    visualTransformation = { priceFilter(it.text) }
                                )
                                Text(
                                    modifier = Modifier.padding(end = 12.dp, start = 4.dp),
                                    text = stringResource(R.string.currency),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = END_PADDING)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .layoutId("merchantReceiptTitle"),
                text = stringResource(R.string.merchant_receipt),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 3.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("noPrintingMerchantReceipt")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = noPrintingMerchantValue, onClick = {
                            noPrintingMerchantValue = true
                           printingMerchantValue  = false
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.failur_to_print_receipt),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("printingMerchantReceipt")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = printingMerchantValue, onClick = {
                            printingMerchantValue = true
                            noPrintingMerchantValue = false
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.print_receipt),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            MainButton(
                modifier = Modifier
                    .mainButtonModifier(isSmall = false)
                    .layoutId("confirm")
            ) {
                showErrorSelectOneOptionToast = false
                amountHasError = false
                val customerOk = noPrintingValue || printReceiptValue
                val merchantOk = printingMerchantValue || noPrintingMerchantValue
                if (!customerOk || !merchantOk) {
                    showErrorSelectOneOptionToast = true
                } else {
                    showConfirmDialog = true
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = if (amountHasError) stringResource(R.string.amount_dont_set)
                else ""
            ) {
                showToast = false
            }
        }
        if (showErrorSelectOneOptionToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = stringResource(R.string.no_options_selected)
            ) {
                showErrorSelectOneOptionToast = false
            }
        }
        if (showAmountIsNotCorrectRangeToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter), message = stringResource(
                    R.string.amount_should_be_greater_than,
                    MAXIMUM_AMOUNT_OF_TRANSACTION.formatAmount()
                )
            ) {
                showAmountIsNotCorrectRangeToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall = false, onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
                val customerStatus = when {
                    noPrintingValue -> PrintStatus.NO_PRINTING
                    printReceiptValue  -> PrintStatus.PRINT
                    else -> PrintStatus.PRINT
                }
                val merchantStatus = when {
                    noPrintingMerchantValue -> PrintStatus.NO_PRINTING
                    printingMerchantValue -> PrintStatus.PRINT
                    else -> PrintStatus.NO_PRINTING
                }
                viewModel.saveReceiptSettings(
                    customerStatus = customerStatus,
                    customerMinAmount = if (autoPrintReceiptValue && amount.isNotBlank()) amount else null,
                    autoPrintReceiptValue,
                    merchantStatus = merchantStatus)
                showConfirmDialog = false
            })
        }
    }
}