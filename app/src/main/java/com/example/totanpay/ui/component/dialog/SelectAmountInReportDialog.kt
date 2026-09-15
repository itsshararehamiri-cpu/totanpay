package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAmountInReportDialog(
    onCancelButtonClicked: () -> Unit, confirmAmount: (String, String) -> Unit
) {
    var fromAmountValue by remember { mutableStateOf("") }
    var toAmountValue by remember { mutableStateOf("") }
    val fromAmountFocusRequester = remember { FocusRequester() }
    val toAmountFocusRequester = remember { FocusRequester() }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        if (keyEvent.key == Key.Enter) {
                            confirmAmount(fromAmountValue, toAmountValue)
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
        ) {
            ConstraintLayout(
                ConstraintSet {
                    val fromAmount = createRefFor("fromAmount")
                    val toAmount = createRefFor("toAmount")
                    val confirm = createRefFor("confirm")
                    constrain(fromAmount) {
                        top.linkTo(parent.top, 10.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(toAmount) {
                        top.linkTo(fromAmount.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(confirm) {
                        top.linkTo(toAmount.bottom, 16.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                }, modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                PriceTextInput(
                    modifier = TextInputModifier
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .focusRequester(fromAmountFocusRequester)
                        .layoutId("fromAmount"),
                    hasError = false,
                    errorMessage = "",
                    title = "",
                    placeholder = stringResource(R.string.from_amount),
                    trailerTitle = stringResource(id = R.string.currency),
                    value = fromAmountValue,
                    onNextClicked = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ) {
                    fromAmountValue = it
                }
                PriceTextInput(
                    modifier = TextInputModifier
                        .padding(start = END_PADDING, end = START_PADDING, top = 2.dp)
                        .focusRequester(toAmountFocusRequester)
                        .layoutId("toAmount"),
                    hasError = false,
                    errorMessage = "",
                    title = "",
                    placeholder = stringResource(R.string.to_amount),
                    trailerTitle = stringResource(id = R.string.currency),
                    value = toAmountValue,
                    onNextClicked = {
                        keyboard?.hide()
                        confirmAmount(fromAmountValue, toAmountValue)
                    }
                ) {
                    toAmountValue = it
                }
                MainButton(
                    modifier = Modifier
                        .padding(horizontal = END_PADDING, vertical = 6.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .layoutId("confirm")
                ) {
                    keyboard?.hide()
                    confirmAmount(fromAmountValue, toAmountValue)
                }
            }
        }
    }
}
