package com.example.totanpay.feature.purchase

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.LocalDeviceManager
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.textFieldModifier
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.component.TextInputContainer
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.priceFilter
import kotlinx.coroutines.launch

@Composable
fun PurchaseIdScreen(onConfirmButtonClicked: (String) -> Unit, onBackButtonClicked: () -> Unit) {
    val context = LocalContext.current
    val device = LocalDeviceManager.current
    var purchaseId: String by remember {
        mutableStateOf("")
    }
    var showError by remember {
        mutableStateOf(false)
    }
    var hasError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val startScan = createRefFor("startScan")
            val purchaseIdContainer = createRefFor("purchaseIdContainer")

            constrain(startScan) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(startScan) {
                top.linkTo(parent.top, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(purchaseIdContainer) {
                top.linkTo(startScan.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )

    ) {

        Box(
            modifier = Modifier.padding(top = 32.dp)
                .clickable {
                    coroutineScope.launch {
                        device.scan(
                            context = context,
                            onSuccess =
                            {
                                purchaseId = it
                            },
                            onTimeout = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onTimeout")
                            },
                            onError = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onError")
                            },
                            onCancel = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onCancel")
                            })
                    }
                }
                .layoutId("startScan")
        ) {
            Image(
                painter = painterResource(id = R.drawable.scan__2_),
                contentDescription = "",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )
            Image(
                painter = painterResource(id = R.drawable.scan__1_),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
        val textFieldValue = TextFieldValue(text =purchaseId, selection = TextRange(purchaseId.length))
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        TextInputContainer(modifier =  Modifier
            .padding(horizontal = 13.dp)
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth().layoutId("purchaseIdContainer"), title = stringResource(R.string.please_enter_purchase_id), hasError = hasError, errorMessage=if(hasError) stringResource(R.string.please_enter_purchase_id) else "",isSmall=true) {
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
                        .textFieldModifier(isSmall = true)
                        .layoutId("textField")
                        .background(MaterialTheme.colorScheme.surface) .focusRequester(focusRequester),
                    value = textFieldValue,
                    onValueChange = {
                        hasError=false
                        if (!it.text.trim().isNotNumber())
                            purchaseId=(it.text.trim())
                    },
                    colors = TextFieldDefaults.colors(
                    )
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
                    keyboardActions = KeyboardActions(onDone = {
                        showError = false
                        if (purchaseId.isNotEmpty())
                            onConfirmButtonClicked(purchaseId)
                        else{
                            showError = true
                        }
                        showError = false
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
            }
        }
    }
}