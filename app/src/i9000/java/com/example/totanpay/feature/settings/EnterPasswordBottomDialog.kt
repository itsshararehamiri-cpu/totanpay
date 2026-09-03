package com.example.totanpay.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.component.CancelButton
import com.example.totanpay.ui.component.Keypad
import com.example.totanpay.ui.component.PasswordDigitPlacement
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT
import com.example.totanpay.ui.theme.MARGIN_SIDE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPasswordBottomDialog(
    modifier: Modifier,
    errorMessage: String ,
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: (String) -> Unit
) {
    var error by remember { mutableStateOf("") }
    val context= LocalContext.current
    LaunchedEffect(errorMessage) {
        error = errorMessage
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val keyboard = LocalSoftwareKeyboardController.current
        var value1: String by remember {
            mutableStateOf("")
        }
        var value2: String by remember {
            mutableStateOf("")
        }
        var value3: String by remember {
            mutableStateOf("")
        }
        var value4: String by remember {
            mutableStateOf("")
        }
        var showError1 by remember {
            mutableStateOf(false)
        }
        var showError2 by remember {
            mutableStateOf(false)
        }
        var showError3 by remember {
            mutableStateOf(false)
        }
        var showError4 by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(Unit) {
            keyboard?.hide()
        }
        Box {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.please_enter_password),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

                )
                Row(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ) {
                    PasswordDigitPlacement(
                        modifier = Modifier,
                        value = value1,
                        showError = showError1
                    )
                    PasswordDigitPlacement(
                        modifier = Modifier,
                        value = value2,
                        showError = showError2
                    )
                    PasswordDigitPlacement(
                        modifier = Modifier,
                        value = value3,
                        showError = showError3
                    )
                    PasswordDigitPlacement(
                        modifier = Modifier,
                        value = value4,
                        showError = showError4
                    )

                }

                Keypad(
                    Modifier
                        .padding(horizontal = MARGIN_SIDE)
                        .padding(top = 45.dp)
                        .fillMaxWidth(), onKeyClicked = {
                        if (value1.isEmpty()) {
                            value1 = it
                        } else {
                            if (value2.isEmpty()) {
                                value2 = it
                            } else {
                                if (value3.isEmpty()) {
                                    value3 = it
                                } else {
                                    if (value4.isEmpty()) {
                                        value4 = it
                                    }  else {
                                    }
                                }
                            }
                        }
                    }, onClearKeyClicked = {
                        if (value1.isNotEmpty() && value2.isNotEmpty() && value3.isNotEmpty() && value4.isNotEmpty())
                            value4 = ""
                        else if (value1.isNotEmpty() && value2.isNotEmpty() && value3.isNotEmpty() && value4.isEmpty())
                            value3 = ""
                        else if (value1.isNotEmpty() && value2.isNotEmpty() && value3.isEmpty() && value4.isEmpty())
                            value2 = ""
                        else if (value1.isNotEmpty() && value2.isEmpty() && value3.isEmpty() && value4.isEmpty())
                            value1 = ""
                    }, onTikKeyClicked = {
                        showError1 = value1.isEmpty()
                        showError2 = value2.isEmpty()
                        showError3 = value3.isEmpty()
                        showError4 = value4.isEmpty()
                        error=""
                        if (value1.isNotEmpty() && value2.isNotEmpty() && value3.isNotEmpty() && value4.isNotEmpty()) {
                            onConfirmButtonClicked("$value1$value2$value3$value4")
                        }
                        else{
                            error=context.getString(R.string.password_not_entered)
                        }

                    })
                CancelButton(
                    modifier = Modifier
                        .padding(horizontal = 0.dp)
                        .padding(top = 32.dp, bottom = 14.dp)
                        .fillMaxWidth()
                        .height(BUTTON_HEIGHT)
                        .padding(horizontal = MARGIN_SIDE)
                ) {
                    onCancelButtonClicked()
                }
            }
            if (error.isNotEmpty()) {
                ShowToast(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .align(Alignment.Center),
                    message = error
                ) {
                    error=""
                }
            }
        }
    }
}