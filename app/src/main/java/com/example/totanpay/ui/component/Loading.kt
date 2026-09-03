package com.example.totanpay.ui.component

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun Loading(waitingTitle:String=stringResource(id = R.string.in_processing)) {
    ConstraintLayout(
        ConstraintSet {
            val loading = createRefFor("loading")
            val inProcessing = createRefFor("inProcessing")
            constrain(loading) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
            constrain(inProcessing) {
                top.linkTo(loading.bottom, 0.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                       // size(Size.ORIGINAL)
                    }).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.padding(top = 0.dp).layoutId("loading").fillMaxWidth().height(LOADING_HEIGHT)
        )
        Text(
            text =waitingTitle,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.layoutId("inProcessing"),
            style = MaterialTheme.typography.displayMedium.copy(fontSize = 20.sp)
        )
    }
}
