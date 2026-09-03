package com.example.totanpay.ui.component

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.resultLogoMoifier
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.Red

@Composable
fun ResultLogo(modifier: Modifier, isSuccess: Boolean = false) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column {
        if (!isSmall(context))
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = if (isSuccess) R.drawable.aa else R.drawable.uns)
                        .apply(block = fun ImageRequest.Builder.() {
                        }).build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = modifier
                    .padding(top = if (isSmall(context)) 0.dp else 2.dp)
            )
        Text(
            modifier = Modifier
                .padding(top = if (isSmall(context)) 1.dp else 5.dp)
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = if (isSuccess) R.string.success_transaction else R.string.unsuccess_transaction),
            color = if (isSuccess) Green50 else Red,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}
