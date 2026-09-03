package com.example.totanpay.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.totanpay.ui.theme.Black400


@Composable
fun ReceivedCodeDigitPlacement(
    showError: Boolean,
    modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        keyboard?.hide()
    }
    var cursorPosition by remember { mutableIntStateOf(0) }
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = value, selection = TextRange(value.length)

            )
        )
    }
    OutlinedTextField(
        value = textFieldValueState,
        onValueChange = {
            if (it.text.length <= 1) {
                textFieldValueState = it
                cursorPosition = it.toString().length
                onValueChange(it.text)
            }
        },
        singleLine = true,
        isError = showError,
        modifier = modifier.size(50.dp),
        colors = OutlinedTextFieldDefaults.colors(
        ).copy(
            focusedContainerColor = Black400,
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedIndicatorColor = White,
            unfocusedIndicatorColor = White,
            errorTextColor = Red
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textDirection = TextDirection.Ltr,
            color = White,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )

}

@Composable
fun ReceivedCodePlacement(
    modifier: Modifier = Modifier,
    showError: Boolean = false,
    hasPhysicalKeyboard: Boolean,
    enteredInput: (String) -> Unit,
    isCompleted: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        keyboard?.hide()
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        var number by remember { mutableIntStateOf(0) }
        val focusManager = LocalFocusManager.current
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
        LaunchedEffect(key1 = digit1) {
            if (digit1.isNotEmpty()) {
                focusManager.moveFocus(focusDirection = FocusDirection.Right)
            } else {
                focusManager.moveFocus(focusDirection = FocusDirection.Left)
            }
        }
        LaunchedEffect(key1 = digit2) {
            if (digit2.isNotEmpty()) {
                focusManager.moveFocus(focusDirection = FocusDirection.Right)
            } else {
                focusManager.moveFocus(focusDirection = FocusDirection.Left)
            }
        }
        LaunchedEffect(
            key1 = digit3,
        ) {
            if (digit3.isNotEmpty()) {
                focusManager.moveFocus(focusDirection = FocusDirection.Right)
            } else {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Left,
                )
            }
        }
        LaunchedEffect(
            key1 = digit4,
        ) {
            if (digit4.isNotEmpty()) {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Next,
                )
            } else {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Left,
                )
            }
        }
        Row(
            modifier = modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val focusRequester1 = remember { FocusRequester() }
            LaunchedEffect(focusRequester1) {
                focusRequester1.requestFocus()
            }
            CustomReceivedCodeDigitPlacement(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .focusRequester(focusRequester1),
                showError = showError,
                hasPhysicalKeyboard = hasPhysicalKeyboard,
                value = digit1
            ) {
                setDigit1(it)
                number = 1
                enteredInput(it)
            }
            CustomReceivedCodeDigitPlacement(
                modifier = Modifier.padding(horizontal = 2.dp),
                hasPhysicalKeyboard = hasPhysicalKeyboard,
                showError = showError,
                value = digit2,

                ) {
                setDigit2(it)
                number = 2
                enteredInput(it)
            }
            CustomReceivedCodeDigitPlacement(
                modifier = Modifier.padding(horizontal = 2.dp),
                hasPhysicalKeyboard = hasPhysicalKeyboard,
                showError = showError,
                value = digit3,
            ) {
                setDigit3(it)
                number = 3
                enteredInput(it)


            }
            CustomReceivedCodeDigitPlacement(
                modifier = Modifier.padding(horizontal = 2.dp),
                hasPhysicalKeyboard = hasPhysicalKeyboard,
                showError = showError,
                value = digit4,
            ) {
                setDigit4(it)
                number = 4
                enteredInput(it)
                val digits = StringBuilder()
                digits.append(digit1)
                digits.append(digit2)
                digits.append(digit3)
                digits.append(it)
                isCompleted(digits.toString())
            }
        }
    }
}



@Composable
fun CustomReceivedCodeDigitPlacement(
    modifier: Modifier = Modifier,
    value: String, showError: Boolean = false,
    hasPhysicalKeyboard: Boolean,
    onValueChange: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        keyboard?.hide()
    }
    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        ReceivedCodeDigitPlacement(
            showError,
            modifier = modifier, value = value, onValueChange = onValueChange
        )
    }

}

