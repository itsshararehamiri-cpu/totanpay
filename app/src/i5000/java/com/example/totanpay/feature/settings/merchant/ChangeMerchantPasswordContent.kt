package com.example.totanpay.feature.settings.merchant

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun ChangeMerchantPasswordContent(
    uiState: ChangeMerchantPasswordUiState,
    onBackClicked: () -> Unit, setMerchantPassword: (String) -> Unit
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var passwordValue: String by remember { mutableStateOf("") }
    var repeatPasswordValue: String by remember { mutableStateOf("") }
    var passwordHasError: Boolean by remember { mutableStateOf(false) }
    var repeatPasswordHaseError: Boolean by remember { mutableStateOf(false) }
    var showConfirmDialog: Boolean by remember {
        mutableStateOf(false)
    }

    var repeatPasswordErrorMessage: String by remember { mutableStateOf("") }
    var passwordErrorMessage: String by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(uiState.confirmSettings) {
        if (uiState.confirmSettings)
            onBackClicked()
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val password = createRefFor("password")
                val repeatPassword = createRefFor("repeatPassword")
                constrain(password) {
                    top.linkTo(parent.top,24.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(repeatPassword) {
                    top.linkTo(password.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }

            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                TextInput(
                    modifier = Modifier
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .layoutId("password"),
                    hasError = passwordHasError, errorMessage = passwordErrorMessage,
                    title = stringResource(id = R.string.password),
                    value = passwordValue, onNextClicked = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }, isSmall = true, onValueChange = {
                        if (!it.trim().isNotNumber())
                            if (it.trim().length <= 4) {
                                passwordErrorMessage = ""
                                passwordHasError = false
                                passwordValue = it
                            } else {
                                passwordErrorMessage =
                                    context.getString(R.string.length_of_pass_be_four)
                                passwordHasError = true
                            }
                    })
            }
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                TextInput(
                    modifier = TextInputModifier
                        .layoutId("repeatPassword"),
                    hasError = repeatPasswordHaseError, errorMessage = repeatPasswordErrorMessage,
                    title = stringResource(id = R.string.repeat_password),
                    value = repeatPasswordValue, isSmall = isSmall(context),
                    onNextClicked = {
                        if (passwordValue.isEmpty() || repeatPasswordValue.isEmpty()) {
                            showToast = true
                        } else if (passwordValue.length < 4 || repeatPasswordValue.length < 4) {
                            showToast = true
                        } else if (passwordValue != repeatPasswordValue) {
                            showToast = true
                        } else {
                            showConfirmDialog = true
                        }
                    }, onValueChange = {
                        if (!it.trim().isNotNumber())
                            if (it.trim().length <= 4) {
                                repeatPasswordErrorMessage = ""
                                repeatPasswordHaseError = false
                                repeatPasswordValue = it
                            } else {
                                repeatPasswordErrorMessage =
                                    context.getString(R.string.length_of_pass_be_four)
                                repeatPasswordHaseError = true
                            }
                    })
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (showToast) {
                    if (passwordValue.isEmpty() || repeatPasswordValue.isEmpty()) "کلمه عبور تعیین نشده است."
                    else if (passwordValue.length < 4 || repeatPasswordValue.length < 4) "طول کلمه عبور باید 4 باشد"
                    else if (passwordValue != repeatPasswordValue) "دو کلمه عبور مطابقت ندارند."
                    else ""
                } else ""
            ) {
                showToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall = true,onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
                setMerchantPassword(passwordValue)
                showConfirmDialog = false

            })
        }

    }
}
