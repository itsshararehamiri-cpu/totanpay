package com.example.totanpay.ui.component.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.common.textFieldModifier
import com.example.totanpay.ui.component.TextInputContainer
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun KeyboardApportiomentDialog(isSmall:Boolean,
    onCancelButtonClicked: () -> Unit, onConfirmButtonClicked: (String) -> Unit
) {
    val context = LocalContext.current
    var percentValue: String by remember {
        mutableStateOf("")
    }
    var showError by remember {
        mutableStateOf(false)
    }
    var hasError by remember {
        mutableStateOf(false)
    }
    var errorMessage: String by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BackHandler {
        onCancelButtonClicked()
    }
    val mainFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        mainFocusRequester.requestFocus()

    }
    val textFieldValue =
        TextFieldValue(text = percentValue, selection = TextRange(percentValue.length))
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val percentTextInput = createRefFor("percentTextInput")
            val confirm = createRefFor("confirm")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                height=if(isSmall)Dimension.value(0.dp) else Dimension.wrapContent

            }
            constrain(percentTextInput) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(confirm) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                height=if(isSmall)Dimension.value(0.dp) else Dimension.wrapContent
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .focusRequester(mainFocusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.Enter) {
                        showError = false
                        hasError=false
                        errorMessage=""
                        if (percentValue.isNotEmpty())
                            onConfirmButtonClicked(percentValue)
                        else{
                            showError = true
                            hasError=true
                            errorMessage=context.getString(R.string.percent_not_entered)
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }

    ) {
        BackButton(
            title = stringResource(id = R.string.account_management),
            modifier = BackButtonModifier.layoutId("toolBar")
        ) {
            onCancelButtonClicked()
        }
        TextInputContainer(
            modifier = Modifier
                .padding(start = END_PADDING, end = START_PADDING, top =if(isSmall)60.dp else 3.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .layoutId("percentTextInput"),
            title = stringResource(R.string.plz_enter_percent),
            hasError = hasError,
            errorMessage,
            isSmall = true
        ) {
            ConstraintLayout(
                ConstraintSet {
                    val trailer = createRefFor("trailer")
                    val textField = createRefFor("textField")
                    constrain(trailer) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    constrain(textField) {
                        top.linkTo(trailer.top)
                        bottom.linkTo(trailer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(trailer.start)
                        width = Dimension.fillToConstraints

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TextField(
                    modifier = Modifier
                        .textFieldModifier(isSmall(context = context))
                        .layoutId("textField")
                        .background(MaterialTheme.colorScheme.surface)
                        .focusRequester(focusRequester),
                    value = textFieldValue,
                    onValueChange = {
                        hasError = false
                        errorMessage = ""
                        if (it.text.isNotEmpty()) {
                            if (it.text.toInt() in 1..100) {
                                percentValue = it.text
                            } else {
                                hasError = true
                                errorMessage = context.getString(R.string.value_should_be_valid)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                    ).copy(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = Transparent,
                        disabledContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        showError = false
                        if (percentValue.isNotEmpty())
                            onConfirmButtonClicked(percentValue)
                        else showError = true
                    }),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        textDirection = TextDirection.Ltr,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
        MainButton(
            Modifier
                .mainButtonModifier(isSmall = isSmall(context = LocalContext.current))
                .layoutId("confirm")
        ) {
            showError = false
            hasError=false
            errorMessage=""
            if (percentValue.isNotEmpty())
                onConfirmButtonClicked(percentValue)
            else{
                showError = true
                hasError=true
                errorMessage=context.getString(R.string.percent_not_entered)
            }
        }
    }
}
