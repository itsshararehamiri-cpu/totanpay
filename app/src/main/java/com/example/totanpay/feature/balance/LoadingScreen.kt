package com.example.totanpay.feature.balance

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun LoadingScreen(
    track2: String,
    pinBlock:String,
    viewModel: BalanceViewModel,
    onSuccessResult: (String) -> Unit,
    onErrorResult:(String)->Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.balance(track2 = track2, pinBlock = pinBlock)
    }
    LaunchedEffect(uiState.isSuccessful) {
        if (uiState.isSuccessful)
        {
            onSuccessResult(uiState.response)
        }
    }
    LaunchedEffect(uiState.isUnSuccessful) {
        if (uiState.isUnSuccessful)
        {
            onErrorResult(uiState.response)
        }
    }
    LoadingContent()
}
@Composable
fun LoadingContent() {
    ConstraintLayout(
        ConstraintSet {
            val loading = createRefFor("loading")
            val inProcessing = createRefFor("in_processing")
            constrain(loading) {
                top.linkTo(parent.top, 90.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(inProcessing) {
                top.linkTo(loading.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = R.drawable.loading)
                    .apply(block = fun ImageRequest.Builder.() {
                        size(Size.ORIGINAL)
                    }).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.layoutId("loading")
        )
        Text(
            text = stringResource(id = R.string.in_processing),
            color = Color.White,
            modifier = Modifier.layoutId("in_processing"),
            style = MaterialTheme.typography.displayLarge
        )
    }
}
@Composable
@Preview
fun LoadingContentPreview() {
    TotanPayTheme {
        LoadingContent()
    }
}
