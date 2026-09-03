package com.example.totanpay.feature.settings.merchant
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton

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
                val toolBar = createRefFor("toolBar")
                val password = createRefFor("password")
                val repeatPassword = createRefFor("repeatPassword")
                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(password) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(repeatPassword) {
                    top.linkTo(password.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
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
                title = stringResource(id = R.string.change_merchant_password),
                modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            TextInput(
                modifier = TextInputModifier
                    .layoutId("password"),
                hasError = passwordHasError, errorMessage = passwordErrorMessage,
                title = stringResource(id = R.string.password),
                value = passwordValue, onNextClicked = {
                    focusManager.moveFocus(FocusDirection.Next)
                }, isSmall = false, onValueChange = {
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
            TextInput(
                modifier = TextInputModifier
                    .layoutId("repeatPassword"),
                hasError = repeatPasswordHaseError, errorMessage = repeatPasswordErrorMessage,
                title = stringResource(id = R.string.repeat_password),
                value = repeatPasswordValue,
                onNextClicked = {
                    keyboard?.hide()
                }, isSmall = false, onValueChange = {
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
            MainButton(
                modifier = Modifier.mainButtonModifier(isSmall = false)
                    .layoutId("confirm")

            ) {
                if (passwordValue.isEmpty() || repeatPasswordValue.isEmpty()) {
                    showToast = true
                } else if (passwordValue.length < 4 || repeatPasswordValue.length < 4) {
                    showToast = true
                } else if (passwordValue != repeatPasswordValue) {
                    showToast = true
                } else {
                    showConfirmDialog = true
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (showToast) {
                    if (passwordValue.isEmpty() || repeatPasswordValue.isEmpty()) stringResource(R.string.no_password_has_been_set)
                    else if (passwordValue.length < 4 || repeatPasswordValue.length < 4)stringResource(R.string.length_of_pass_be_four)
                    else if (passwordValue != repeatPasswordValue) stringResource(R.string.two_password_is_not_same)
                    else ""
                } else ""
            ) {
                showToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall = false,onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
                setMerchantPassword( passwordValue)
                showConfirmDialog = false

            })
        }

    }
}
