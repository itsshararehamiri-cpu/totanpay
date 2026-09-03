package com.example.totanpay.ui.component.dialog

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.ui.DialogContainerModifier
import com.example.totanpay.ui.component.button.MainButton


@Composable
fun ShouldChangePasswordMessageDialog(onConfirm:()-> Unit) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = DialogContainerModifier().wrapContentHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                    .data(data = R.drawable.ic_danger)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        size(Size.ORIGINAL)
                                    }).build(),
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 35.dp)
                                .align(Alignment.CenterHorizontally)
                                .size(70.dp)
                        )
                        Text(
                            text = stringResource(R.string.should_change_pass),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth()
                        )
                        MainButton(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth()
                                .mainButtonModifier(isSmall = false), onClick = {
                                onConfirm()
                            })
                    }
                }
        }
}
