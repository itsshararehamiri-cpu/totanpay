package com.example.totanpay.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.common.textFieldModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.priceFilter

@Composable
fun PurchasePriceTextInput(
    modifier: Modifier,
    title: String,
    trailerTitle: String,
    value: String,
    errorMessage: String = "",
    hasError: Boolean,
    isSmall:Boolean=false,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))
    TextInputContainer(modifier = modifier, title = title, hasError = hasError, errorMessage,isSmall=isSmall) {
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
                    width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(
                modifier = Modifier
                    .textFieldModifier(isSmall = isSmall)
                    .layoutId("textField")
                    .background(MaterialTheme.colorScheme.surface)
,                value = textFieldValue,
                onValueChange = {
                    if (!it.text.trim().isNotNumber())
                        onValueChange(it.text.trim())
                },
                colors = TextFieldDefaults.colors().copy(
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
                keyboardActions = KeyboardActions(onDone = {
                    onDone()
                }),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium .copy(
                    textDirection = TextDirection.Ltr,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                visualTransformation = { annotatedString ->
                    priceFilter(annotatedString.text)
                },
            )
            Text(
                modifier = Modifier
                    .padding(end = 15.dp, start = 4.dp)
                    .layoutId("trailer")
                    .wrapContentWidth(),
                text = trailerTitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}