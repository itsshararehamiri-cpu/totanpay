package com.example.totanpay.feature.voucher

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.totanpay.ui.component.CancelButton
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.MARGIN_SIDE_MAIN_CONFIRM
import com.example.totanpay.ui.theme.Orange
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ReadCardScreen(
    viewModel: ReadCardViewModel,
    onGetTrack2: (String) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.track2) {
        if (uiState.track2.isNotEmpty()) {
            onGetTrack2(uiState.track2)
        }
    }
    BackHandler {
        onBackButtonClicked()
    }
    ReadCardContent(onBackButtonClicked = { onBackButtonClicked() })

}

@Composable
fun ReadCardContent(onBackButtonClicked: () -> Unit) {

    ConstraintLayout(
        ConstraintSet {
            val plzSwipeCard = createRefFor("plzSwipeCard")
            val cardSwipeImage = createRefFor("cardSwipeImage")
            val cancelButton = createRefFor("cancelButton")
            constrain(plzSwipeCard) {
                top.linkTo(parent.top, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(cardSwipeImage) {
                top.linkTo(plzSwipeCard.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(cancelButton) {
                bottom.linkTo(parent.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }

        }, modifier = Modifier
            .fillMaxSize()
            .background(Orange)
    ) {
        Text(
            text = stringResource(id = R.string.plz_swipe_card),
            color = Color.White,
            modifier = Modifier.layoutId("plzSwipeCard"),
            style = MaterialTheme.typography.displayLarge
        )
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
                    .data(data = R.drawable.card_swipe)
                    .apply(block = fun ImageRequest.Builder.() {
                        size(Size.ORIGINAL)
                    }).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.layoutId("cardSwipeImage")
        )
        CancelButton(
            title = stringResource(id = R.string.cancel),
            modifier = Modifier
                .layoutId("cancelButton")

                .padding(top = 18.dp)
                .padding(
                    bottom = MARGIN_BOTTOM_MAIN_CONFIRM,
                    start = MARGIN_SIDE_MAIN_CONFIRM,
                    end = MARGIN_SIDE_MAIN_CONFIRM
                )
                .fillMaxWidth()
        ){
            onBackButtonClicked()
        }
    }

}

@Composable
@Preview
fun ReadCardScreenPreview() {
    TotanPayTheme {
        ReadCardContent(onBackButtonClicked = {})
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(File("E:\\myprojects\\TotanPay\\app\\src\\main\\res\\drawable\\card_swipe.gif"))
//            .build(),
//        contentDescription = null,
//        modifier = Modifier.size(300.dp).layoutId("cardSwipeImage")
//    )
    }
}
