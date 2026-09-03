package com.example.totanpay.feature.settings.supervisor


import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.ui.component.Keypad
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.dialog.MessageDialog

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
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(
        modifier =  Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val pin = createRefFor("pin")
                val title = createRefFor("title")
                val insertKeyImage = createRefFor("insertKeyImage")
                val plzInsertCard = createRefFor("plzInsertCard")
                val plzExitCard = createRefFor("plzExitCard")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(title) {
                    top.linkTo(toolBar.bottom, 3.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(pin) {
                    top.linkTo(title.bottom, 3.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(insertKeyImage) {
                    top.linkTo(toolBar.bottom, 3.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(plzInsertCard) {
                    top.linkTo(insertKeyImage.bottom, 3.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(plzExitCard) {
                    top.linkTo(insertKeyImage.bottom, 3.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
            }, modifier = Modifier
                .fillMaxSize()
        ) {
            BackButton(
                title = stringResource(id = R.string.key_injection), modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            if ((uiState.showEnterFirstCard || uiState.showEnterSecondCard)&& uiState.errorInKeyInjection.isEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = R.drawable.insert_card)
                            .apply(block = fun ImageRequest.Builder.() {
                                size(Size.ORIGINAL)
                            }).build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier.layoutId("insertKeyImage")
                )
                Text(
                    text = if (uiState.showEnterFirstCard) stringResource(R.string.please_enter_first_card)
                    else stringResource(R.string.please_enter_second_card),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("plzInsertCard"),
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp)
                )
            }
            if (uiState.showRemoveFirstCard || uiState.showRemoveSecondCard)
            {
                Text(
                    text = if (uiState.showRemoveFirstCard){
                        stringResource(R.string.please_remove_first_card)
                    }
                    else
                    {
                        stringResource(R.string.please_remove_second_card)
                    },
                    color =  MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("plzExitCard"),
                    style = MaterialTheme.typography.displayLarge
                )
            }
            if (isFirstPinKey || isSecondPinKey) {
                Text(
                    text = if (isFirstPinKey) {
                        stringResource(R.string.please_enter_first_password)
                    } else{
                        stringResource(R.string.please_enter_second_password)
                    },
                    color =   MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("title"),  style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)
                        )
                        .layoutId("pin"), Arrangement.Start
                ) {
                    Text(
                        text = if (isFirstPinKey) {
                            firstPinKey
                        } else if (isSecondPinKey) {
                            secondPinKey
                        } else "",
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth() .padding(top = 15.dp, bottom = 15.dp, end = 5.dp)
                        ,
                        color =  MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.End
                    )
                }
            }
        }
        if (uiState.showEnterFirstPin || uiState.showEnterSecondPin) {
            Keypad(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter), onKeyClicked = {
                    if (isFirstPinKey) {
                        firstPinKey = "$firstPinKey$it"
                    }
                    if (isSecondPinKey) {
                        secondPinKey = "$secondPinKey$it"
                    }
                }, onClearKeyClicked = {
                    if (isFirstPinKey) {
                        val temp = firstPinKey.dropLast(1)
                        firstPinKey = temp
                    }
                    if (isSecondPinKey) {
                        val temp = secondPinKey.dropLast(1)
                        secondPinKey = temp
                    }
                }, onTikKeyClicked = {
                    if (isFirstPinKey) {
                        viewModel.verifyFirstPin(firstPinKey.trim(), context = context)
                    }
                    if (isSecondPinKey) {
                        viewModel.verifySecondPin(secondPinKey.trim(), context = context)
                    }
                }
            )
        }
        if (uiState.showKeyInjectionIsSucceed) {
            MessageDialog(
                isVisible = uiState.showKeyInjectionIsSucceed,
                message = stringResource(id = R.string.key_injection_done_successfully),
                imageId = R.drawable.aa
            )
        }
        if(uiState.errorInKeyInjection.isNotEmpty()){
            MessageDialog(
                isVisible = uiState.errorInKeyInjection.isNotEmpty(),
                message = uiState.errorInKeyInjection,
                imageId = R.drawable.uns
            )
        }
    }
}