package com.example.totanpay.feature

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.ui.component.Version
import com.example.totanpay.ui.component.dialog.SettingsDataTimeDialog
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.util.TimeSettingsChecker
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onShow: () -> Unit) {
    val context = LocalContext.current
    var navigate by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessageLocation by remember { mutableStateOf<String?>(null) }
    var showDialogLocation by remember { mutableStateOf(false) }
    val imageLoader = ImageLoader.Builder(context).components {
        if (SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isAutoTime = TimeSettingsChecker.isAutoDateTimeEnabled(context)
                val isTehran = TimeSettingsChecker.isTimeZoneTehran()
                if (isAutoTime) {
                    errorMessage = "لطفاً تنظیم خودکار تاریخ و ساعت را خاموش کنید."
                    showDialog = true
                } else if (!isTehran) {
                    errorMessage=""
                    showDialog=false
                    errorMessageLocation = "لطفا محدوده زمانی دستگاه را روی تهران تنظیم نمایید"
                    showDialogLocation = true
                } else {
                    errorMessage=""
                    showDialog=false
                    errorMessageLocation=""
                    showDialogLocation=false
                    navigate=true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(navigate) {
        if (navigate) {
            delay(3000)
            onShow()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ConstraintLayout(
            ConstraintSet {
                val totanLogo = createRefFor("totanLogo")
                val progress = createRefFor("progress")
                val version = createRefFor("version")
                constrain(totanLogo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = androidx.constraintlayout.compose.Dimension.fillToConstraints
                    width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                }
                constrain(version) {
                    bottom.linkTo(totanLogo.bottom)
                    start.linkTo(totanLogo.start)
                    end.linkTo(totanLogo.end)
                }
                constrain(progress) {
                    start.linkTo(totanLogo.start)
                    end.linkTo(totanLogo.end)
                    bottom.linkTo(version.top)
                }
            }, modifier = Modifier.fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .layoutId("totanLogo")
                    .align(Alignment.Center), painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = R.drawable.splash_gif)
                        .build(), imageLoader = imageLoader
                ), contentDescription = "", contentScale = ContentScale.FillWidth
            )
            if (!isSmall(context))
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = R.drawable.loading)
                            .apply(block = fun ImageRequest.Builder.() {
                            }).build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            if (isSmall(context)) 90.dp else
                                150.dp
                        )
                        .layoutId("progress")
                )
            Version(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .wrapContentWidth()
                    .layoutId("version")
            )
        }
        if(showDialog){
            SettingsDataTimeDialog(titleMessage = errorMessage?:"", onDismiss = {
                showDialog=false
            }, onConfirmButtonClicked = {
                val intent = Intent(Settings.ACTION_DATE_SETTINGS)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context. startActivity(intent)
            })
        }
        if(showDialogLocation){
            SettingsDataTimeDialog(titleMessage = errorMessageLocation?:"", onDismiss = {
                showDialogLocation=false
            }, onConfirmButtonClicked = {
                val intent = Intent(Settings.ACTION_DATE_SETTINGS)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context. startActivity(intent)
            })
        }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    TotanPayTheme {
        SplashScreen {}
    }
}