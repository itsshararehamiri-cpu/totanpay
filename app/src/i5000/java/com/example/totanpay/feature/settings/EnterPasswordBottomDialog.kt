package com.example.totanpay.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.component.ShowToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPasswordBottomDialog(
    modifier: Modifier,
    errorMessage: String = "",
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: (String) -> Unit
) {
    var showToast by remember { mutableStateOf(false) }
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            showToast = true
        }
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val (digit1, setDigit1) = remember {
            mutableStateOf("")
        }
        val (digit2, setDigit2) = remember {
            mutableStateOf("")
        }
        val (digit3, setDigit3) = remember {
            mutableStateOf("")
        }
        val (digit4, setDigit4) = remember {
            mutableStateOf("")
        }
        val enteredPassword = "$digit1$digit2$digit3$digit4"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
            ConstraintLayout(
                constraintSet = ConstraintSet {
                    val title = createRefFor("title")
                    val password = createRefFor("password")
                    constrain(title) {
                        top.linkTo(parent.top, 28.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    constrain(password) {
                        top.linkTo(title.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                },
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.please_enter_password),
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth()
                        .layoutId("title"),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge .copy(fontWeight = FontWeight.ExtraBold)

                )
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .wrapContentWidth()
                            .padding(horizontal = 20.dp)
                            .layoutId("password").align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val focusRequester1 = remember { FocusRequester() }
                        val focusRequester2 = remember { FocusRequester() }
                        val focusRequester3 = remember { FocusRequester() }
                        val focusRequester4 = remember { FocusRequester() }
                        LaunchedEffect(Unit) {
                            focusRequester1.requestFocus()
                        }
                        CustomReceivedCodeDigitPlacement2(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .focusRequester(focusRequester1),
                            onDone = {
                                if (enteredPassword.length == 4) {
                                    onConfirmButtonClicked(enteredPassword)
                                } else {
                                    showToast=true
                                }
                            },
                            value = digit1
                        ) {
                            setDigit1(it)
                            if (it.isNotEmpty()) focusRequester2.requestFocus()
                        }
                        CustomReceivedCodeDigitPlacement2(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .focusRequester(focusRequester2),
                            onDone = {
                                if (enteredPassword.length == 4) {
                                    onConfirmButtonClicked(enteredPassword)
                                } else {
                                    showToast=true
                                }
                            },
                            value = digit2,

                            ) {
                            setDigit2(it)
                            if (it.isNotEmpty()) focusRequester3.requestFocus()
                            else focusRequester1.requestFocus()
                        }
                        CustomReceivedCodeDigitPlacement2(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .focusRequester(focusRequester3),
                            value = digit3,
                            onDone = {
                                if (enteredPassword.length == 4) {
                                    onConfirmButtonClicked(enteredPassword)
                                } else {
                                   showToast=true
                                }
                            }
                        ) {
                            setDigit3(it)
                            if (it.isNotEmpty()) focusRequester4.requestFocus()
                            else focusRequester2.requestFocus()
                        }
                        CustomReceivedCodeDigitPlacement2(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .focusRequester(focusRequester4),
                            value = digit4,
                            onDone = {
                                if (enteredPassword.length == 4) {
                                    onConfirmButtonClicked(enteredPassword)
                                } else {
                                    showToast=true
                                }
                            }
                        ) {
                            setDigit4(it)
                            if (it.isEmpty()) focusRequester3.requestFocus()
                        }
                    }
                }

            }
            if (showToast) {
                ShowToast(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .align(Alignment.Center),
                    message = errorMessage.ifEmpty { stringResource(R.string.please_enter_password) }
                ) {
                    showToast = false
                }
            }
        }

    }
}

@Composable
fun CustomReceivedCodeDigitPlacement2(
    modifier: Modifier = Modifier,
    value: String,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                // Always keep the most recently typed digit: the cursor position after an
                // automatic focus change is not guaranteed to be at the end of the field, so a
                // new keystroke can land before the existing digit (e.g. "5" -> "65"). Taking the
                // last digit instead of rejecting multi-character input is what makes clearing a
                // box and typing a new digit into it work reliably.
                onValueChange(it.filter { char -> char.isDigit() }.takeLast(1))
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = modifier
                .size(56.dp),
            colors = TextFieldDefaults.colors().copy(
                disabledTextColor = Color.Gray,
                disabledContainerColor = Color.White,
                cursorColor = Color.Blue,
                errorCursorColor = Color.Red
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textDirection = TextDirection.Ltr,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
               textAlign = TextAlign.Center
            ), keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
            })
        )
    }

}