package com.example.totanpay.ui.component

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.example.totanpay.ui.theme.White300

@Composable
fun CenteredDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .background(White100)
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
                                .data(data = R.drawable.aa)
                                .apply(block = fun ImageRequest.Builder.() {
                                    size(Size.ORIGINAL)
                                }).build(),
                            imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(50.dp)
                            .layoutId("successTickImage")
                    )
                    Text(
                        text = "تراکنش با موفقیت انجام شد",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Transparent background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
                .clickable { onDismissRequest() }
        )
    }
}
@Composable
fun BlurredDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
                .height(200.dp)
                .clickable { onDismissRequest() }
                .background(color = Color.Black.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(8.dp))
        ) {
            Dialog(
                onDismissRequest = onDismissRequest,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 20.dp)
                .background(color = Color.Transparent)
        )
    }
}
@Composable
@Preview
fun BlurredDialogPreview(){
    TotanPayTheme {
        BlurredDialog(isVisible = true, onDismissRequest = {}){
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(16.dp)).background(White300.copy(alpha = 0.15f))
                    .blur(15.dp)
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
                            .data(data = R.drawable.aa)
                            .apply(block = fun ImageRequest.Builder.() {
                                size(Size.ORIGINAL)
                            }).build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 20.dp).align(Alignment.CenterHorizontally)
                        .size(50.dp)
                )
                Text(
                    text = "پیکربندی با موفقیت انجام شد",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center, modifier = Modifier.padding(top = 20.dp, bottom = 10.dp).fillMaxWidth()
                )
            }
        }
    }
}
@Composable
fun MessageDialog(isVisible: Boolean,message:String,imageId:Int){
    BlurredDialog(isVisible = isVisible, onDismissRequest = {}){
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(16.dp)).background(White300.copy(alpha = 0.10f)).blur(15.dp).shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = true
                )
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
                        .data(data = imageId)
                        .apply(block = fun ImageRequest.Builder.() {
                            size(Size.ORIGINAL)
                        }).build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier.padding(top = 20.dp).align(Alignment.CenterHorizontally)
                    .size(50.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White.copy(alpha = 0.72f),
                textAlign = TextAlign.Center, modifier = Modifier.padding(top = 20.dp, bottom = 10.dp).fillMaxWidth()
            )
        }
    }
}