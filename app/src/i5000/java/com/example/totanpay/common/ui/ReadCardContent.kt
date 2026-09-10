package com.example.totanpay.common.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.multidex.BuildConfig.FLAVOR
import com.example.totanpay.R
import com.example.totanpay.TIME_TO_FINISH_TAKE_CARD
import com.example.totanpay.common.CountdownEffect
import com.example.totanpay.common.PlaybackSoundEffect
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.component.ShowErrorMessage
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.DeepBlue
import com.example.totanpay.ui.theme.Dimensions.PSP_LOGO_hEIGHT_RECEPINT
import com.example.totanpay.ui.theme.MARGIN_SIDE
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ReadCardContent(
    uiState: ReadCardUiState,
    amountValue: String? = null,
    amountTitle: String? = null,
    extraMessageValue: String? = null,
    showFee: Boolean = false,
    readCard: () -> Unit,
    hideInternetErrorMessage:()->Unit,
    onBackButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            readCard()
        }
    }
    PlaybackSoundEffect(uiState.playbackSound,R.raw.cardswipe)
    CountdownEffect(TIME_TO_FINISH_TAKE_CARD) {
        onBackButtonClicked()
    }
    BackHandler {
        onBackButtonClicked()
    }
    Box (modifier = Modifier){
        ConstraintLayout(
            ConstraintSet {
                val topImageCardSwipe = createRefFor("topImageCardSwipe")
                val plzSwipeCard = createRefFor("plzSwipeCard")
                val fee = createRefFor("fee")
                val amount = createRefFor("amount")
                val extraMessage = createRefFor("extraMessage")
                val logos = createRefFor("logos")
//                val merchantNameImage = createRefFor("merchantNameImage")
//                val merchantPhoneImage = createRefFor("merchantPhoneImage")
                val merchantName = createRefFor("merchantName")
                val merchantPhone = createRefFor("merchantPhone")
                constrain(topImageCardSwipe) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(logos) {
                    top.linkTo(topImageCardSwipe.top)
                    end.linkTo(topImageCardSwipe.end)
                    start.linkTo(topImageCardSwipe.start)
                    bottom.linkTo(topImageCardSwipe.bottom)
                }
                constrain(plzSwipeCard) {
                    top.linkTo(topImageCardSwipe.bottom, 16.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(fee) {
                    top.linkTo(plzSwipeCard.bottom, 6.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(amount) {
                    top.linkTo(plzSwipeCard.bottom, 6.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(extraMessage) {
                    top.linkTo(amount.bottom, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
//                constrain(merchantNameImage) {
//                    bottom.linkTo(parent.bottom, 20.dp)
//                    start.linkTo(plzSwipeCard.start, 55.dp)
//                }
                constrain(merchantName) {
                    bottom.linkTo(merchantPhone.top
                        , 5.dp)
                    start.linkTo(plzSwipeCard.start)
                    end.linkTo(plzSwipeCard.end)
                }
//                constrain(merchantPhoneImage) {
//                    bottom.linkTo(merchantNameImage.top, 6.dp)
//                    end.linkTo(merchantNameImage.end)
//                    start.linkTo(merchantNameImage.start)
//                }
                constrain(merchantPhone) {
                    bottom.linkTo(parent.bottom, 14.dp)
                    start.linkTo(plzSwipeCard.start)
                    end.linkTo(plzSwipeCard.end)
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_top_image_card_swipe),
                contentDescription = "",
                modifier = Modifier
                    .height(85.dp)
                    .fillMaxWidth()
                    .layoutId("topImageCardSwipe"),
                contentScale = ContentScale.FillBounds
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = MARGIN_SIDE)
                    .fillMaxWidth()
                    .layoutId("logos")
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(
                        PSP_LOGO_hEIGHT_RECEPINT
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_shapark),
                    contentDescription = "",
                    modifier = Modifier
                        .height(
                            48.dp
                        )
                        .width(88.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.weight(1f))
                if (FLAVOR == "pn")
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_white_pn_logo
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .height(25.dp)
                            .width(124.dp),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentScale = ContentScale.FillBounds
                    )
                else Image(
                    painter = painterResource(
                        id = R.drawable.fanava_logo
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .height(25.dp)
                        .width(120.dp),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.FillBounds
                )

            }
            Text(
                text = stringResource(id = R.string.plz_swipe_card),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.layoutId("plzSwipeCard"),
                style = MaterialTheme.typography.displayLarge
            )
            if (showFee) {
                Row(modifier = Modifier.layoutId("fee")) {
                    Text(
                        text = stringResource(R.string.balance_transaction_fee),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "1440".formatAmount(),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = stringResource(R.string.currency),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            if (amountValue != null && amountTitle!=null) {
                Row(modifier = Modifier.layoutId("amount")) {
                    Text(
                        text =amountTitle,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = amountValue.formatAmount(),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = stringResource(R.string.currency),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier =  Modifier.padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            if(extraMessageValue!=null){
                Text(
                    text =extraMessageValue,
                    color = DeepBlue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.layoutId("extraMessage"),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.ic_shop),
//                contentDescription = uiState.merchantName,
//                modifier = Modifier
//                    .size(16.dp)
//                    .layoutId("merchantNameImage"),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_mobile),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(16.dp)
//                    .layoutId("merchantPhoneImage"),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
//            )
            Text(
                text = uiState.merchantName,
                modifier = Modifier.layoutId("merchantName"),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = uiState.merchantPhone,
                modifier = Modifier
                    .layoutId("merchantPhone")
                    .wrapContentWidth(),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        if(uiState.showInternetErrorMessage){
            ShowToast(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                message =  context.getString(R.string.values_are_not_entered)
            ) {
                hideInternetErrorMessage()
            }
        }
        if(uiState.showInternetErrorMessage){
            ShowErrorMessage(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                message =  context.getString(R.string.switch_is_not_accessible)
            ) {
                hideInternetErrorMessage()
                onBackButtonClicked()
            }
        }
    }

}

@Composable
@Preview
fun SmallReadCardContentPreview() {
    TotanPayTheme {
        ReadCardContent(
            readCard = {},
            hideInternetErrorMessage = {},
            uiState = ReadCardUiState
                (
                merchantName = "تعاونی مصرف فرهنگیان تهران",
                merchantPhone = "0218234232"
            )
        ) {

        }
    }
}