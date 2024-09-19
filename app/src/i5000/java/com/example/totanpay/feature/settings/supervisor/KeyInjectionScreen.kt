package com.example.totanpay.feature.settings.supervisor


import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.textFieldModifier
import com.example.totanpay.ui.component.TextInputContainer
import com.example.totanpay.ui.component.dialog.MessageDialog
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun KeyInjectionScreen(viewModel: KeyInjectionViewModel, onBackClicked: () -> Unit) {
    val context=LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var firstPinKey by remember {
        mutableStateOf("")
    }
    var secondPinKey by remember {
        mutableStateOf("")
    }
    var isFirstPinKey by remember {
        mutableStateOf(true)
    }
    var isSecondPinKey by remember {
        mutableStateOf(false)
    }
    var cursorPosition by remember { mutableIntStateOf(0) }
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                ""
            )
        )
    }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            onBackClicked()
        }
    }
    LaunchedEffect(uiState.showEnterFirstPin) {
        isFirstPinKey = uiState.showEnterFirstPin
    }
    LaunchedEffect(uiState.showEnterSecondPin) {
        if (uiState.showEnterSecondPin) {
            isFirstPinKey = false
            isSecondPinKey = true
            firstPinKey = ""
            secondPinKey = ""
        } else {
            isSecondPinKey = false
        }
    }
    val imageLoader = ImageLoader.Builder(context).components {
        if (SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
        ConstraintLayout(
            ConstraintSet {
                val pin = createRefFor("pin")
                val insertKeyImage = createRefFor("insertKeyImage")
                val plzInsertCard = createRefFor("plzInsertCard")
                val plzExitCard = createRefFor("plzExitCard")
                constrain(pin) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                constrain(insertKeyImage) {
                    top.linkTo(parent.top,10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(plzInsertCard) {
                    top.linkTo(insertKeyImage.bottom, 10.dp)
                    end.linkTo(pin.end)
                    start.linkTo(pin.start)
                }
                constrain(plzExitCard) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
            }, modifier = Modifier.fillMaxSize()
        ) {
            if ((uiState.showEnterFirstCard || uiState.showEnterSecondCard) && uiState.errorInKeyInjection.isEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(data = R.drawable.insert_card)
                            .apply(block = fun ImageRequest.Builder.() {
                                size(Size(200, 180))
                            }).build(), imageLoader = imageLoader
                    ), contentDescription = null, modifier = Modifier.layoutId("insertKeyImage")
                )
                Text(
                    text = if (uiState.showEnterFirstCard) stringResource(R.string.please_enter_first_card)
                    else stringResource(R.string.please_enter_second_card),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("plzInsertCard"),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            if (uiState.showRemoveFirstCard || uiState.showRemoveSecondCard) {
                Text(
                    text = if (uiState.showRemoveFirstCard) {
                        stringResource(R.string.please_remove_first_card)
                    } else {
                        stringResource(R.string.please_remove_second_card)
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("plzExitCard"),
                    style = MaterialTheme.typography.displayLarge
                )
            }
            if (isFirstPinKey || isSecondPinKey) {
                TextInputContainer(
                    modifier = Modifier
                        .padding(start = END_PADDING, end = START_PADDING, top = 3.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .layoutId("pin")
                    , title = if (isFirstPinKey) {
                        stringResource(R.string.please_enter_first_password)
                    } else {
                        stringResource(R.string.please_enter_second_password)
                    }, hasError = hasError, errorMessage, isSmall = true
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
                            value = textFieldValueState,
                            onValueChange = {
                                textFieldValueState = it
                                cursorPosition = it.toString().length
                                if (isFirstPinKey) {
                                    firstPinKey = it.text
                                } else if (isSecondPinKey) {
                                    secondPinKey = it.text
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
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                hasError = false
                                errorMessage = ""
                                if ((isFirstPinKey && firstPinKey.isNotEmpty()) || (isSecondPinKey && secondPinKey.isNotEmpty())) {
                                    if (isFirstPinKey) {
                                        viewModel.verifyFirstPin(firstPinKey.trim())
                                        firstPinKey = ""
                                        textFieldValueState = TextFieldValue(
                                            ""
                                        )
                                    }
                                    if (isSecondPinKey) {
                                        viewModel.verifySecondPin(secondPinKey.trim())
                                        secondPinKey = ""
                                    }
                                } else {
                                    if (isFirstPinKey && firstPinKey.isEmpty()) {
                                        hasError = true
                                        errorMessage =
                                            context.getString(R.string.please_enter_first_password)
                                    }
                                    if (isSecondPinKey && secondPinKey.isEmpty()) {
                                        hasError = true
                                        errorMessage =
                                            context.getString(R.string.please_enter_second_password)
                                    }
                                }
                            }),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                textDirection = TextDirection.Ltr,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
            if (uiState.showKeyInjectionIsSucceed) {
                MessageDialog(
                    isVisible = uiState.showKeyInjectionIsSucceed,
                    message = stringResource(id = R.string.key_injection_done_successfully),
                    imageId = R.drawable.aa
                )
            }
            if (uiState.errorInKeyInjection.isNotEmpty()) {
                MessageDialog(
                    isVisible = uiState.errorInKeyInjection.isNotEmpty(),
                    message = uiState.errorInKeyInjection,
                    imageId = R.drawable.uns
                )
            }
        }
    }
}