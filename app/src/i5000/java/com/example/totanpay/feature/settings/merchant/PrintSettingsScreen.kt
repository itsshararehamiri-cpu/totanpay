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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.R
import com.example.totanpay.data.repository.PrintStatus
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.PurchasePriceTextInput
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import kotlinx.coroutines.launch

@Composable
fun PrintSettingsScreen(viewModel: PrintingSettingsViewModel, onBackButtonClicked: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var amount: String by remember { mutableStateOf("") }
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    var showErrorSelectOneOptionToast by remember { mutableStateOf(false) }

    var noPrintingValue: Boolean by remember { mutableStateOf(false) }
    var alwaysPrintingValue: Boolean by remember { mutableStateOf(true) }
    var printingWithMinAmountValue: Boolean by remember { mutableStateOf(false) }

    var showConfirmDialog: Boolean by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        amount = uiState.minimumAmountForPrint
        when (uiState.printStatus) {
            PrintStatus.NO_PRINTING -> {
                noPrintingValue = true
                alwaysPrintingValue = false
                printingWithMinAmountValue = false
            }

            PrintStatus.ALWAYS_PRINTING -> {
                noPrintingValue = false
                alwaysPrintingValue = true
                printingWithMinAmountValue = false
            }

            PrintStatus.PRINTING_WITH_MIN_AMOUNT -> {
                noPrintingValue = false
                alwaysPrintingValue = false
                printingWithMinAmountValue = true
            }
        }
    }
    LaunchedEffect(uiState.confirmSettings) {
        if (uiState.confirmSettings) onBackButtonClicked()
    }
    val scrollState=rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(modifier = Modifier.fillMaxSize() .verticalScroll(scrollState)

        .focusRequester(focusRequester)
        .focusable()
        .onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.key == Key.Enter) {
                    showErrorSelectOneOptionToast = false
                    amountHasError = false
                    if (!printingWithMinAmountValue && !alwaysPrintingValue && !noPrintingValue) {
                        showErrorSelectOneOptionToast = true
                    } else {
                        if (printingWithMinAmountValue && amount.isEmpty()) {
                            amountHasError = true
                            showToast = true
                        } else {
                            showConfirmDialog = true
                        }
                    }
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }) {
        Column( modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier  .clip(RoundedCornerShape(8.dp))
                    .padding(start = END_PADDING, end = START_PADDING, top = 60.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(8.dp)
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
                            noPrintingValue = !noPrintingValue
                            if (noPrintingValue) {
                                alwaysPrintingValue = false
                                printingWithMinAmountValue = false
                            }
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = "عدم چاپ رسید",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier  = Modifier  .clip(RoundedCornerShape(8.dp))
                    .padding(start = END_PADDING, end = START_PADDING,top=10.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("alwaysPrinting")

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = alwaysPrintingValue, onClick = {
                            alwaysPrintingValue = !alwaysPrintingValue
                            if (alwaysPrintingValue) {
                                noPrintingValue = false
                                printingWithMinAmountValue = false
                            }
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray // Color when unselected
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = "چاپ رسید با هر مبلغ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier = Modifier  .clip(RoundedCornerShape(8.dp))
                    .padding(start = END_PADDING, end = START_PADDING,top=10.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("printingWithMinAmount")
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = printingWithMinAmountValue, onClick = {
                                coroutineScope.launch {
                                    scrollState.scrollTo(50)
                                }
                                printingWithMinAmountValue = !printingWithMinAmountValue
                                if (printingWithMinAmountValue) {
                                    alwaysPrintingValue = false
                                    noPrintingValue = false
                                }
                            }, colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = androidx.compose.ui.graphics.Color.Gray // Color when unselected
                            )
                        )
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = "چاپ رسید با تعیین حداقل مبلغ",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(1f))

                    }
                    PurchasePriceTextInput(modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(horizontal = 15.dp)
                        .height(100.dp)
                        .fillMaxWidth(),
                        hasError = amountHasError,
                        errorMessage = "مبلغ  را وارد نمایید:",
                        title = "لطفا حداقل مبلغ برای چاپ رسید را تعیین نمایید:",
                        trailerTitle = stringResource(id = R.string.currency),
                        value = amount,
                        onDone = {
                            showErrorSelectOneOptionToast = false
                            amountHasError = false
                            if (!printingWithMinAmountValue && !alwaysPrintingValue && !noPrintingValue) {
                                showErrorSelectOneOptionToast = true
                            } else {
                                if (printingWithMinAmountValue && amount.isEmpty()) {
                                    amountHasError = true
                                    showToast = true
                                } else {
                                    showConfirmDialog = true
                                }
                            }
                        }) {
                        showToast = false
                        amount = it
                    }
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = if (amountHasError) "مبلغ تعیین نشده است."
                else ""
            ) {
                showToast = false
            }
        }
        if (showErrorSelectOneOptionToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = "هیچ گزینه ای انتخاب نشده است"
            ) {
                showToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall =true , onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
                if (noPrintingValue) viewModel.setNoPrinting()
                else if (alwaysPrintingValue) viewModel.setAlwaysPrinting()
                else viewModel.setPrintingWithMinAmount(minAmount = amount)
                showConfirmDialog = false
            })
        }
    }
}

@Composable
@Preview
fun PrintSettingsScreenPreview() {
    TotanPayTheme {
        PrintSettingsScreen(hiltViewModel()) {}
    }
}