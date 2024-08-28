package com.example.totanpay

//import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.ui.component.HambergerButton
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.component.MenuItem
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.Black200
import com.example.totanpay.ui.theme.Blue100
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White200

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onPurchaseSelected: (String) -> Unit,
    onBalanceSelected: () -> Unit, onBillPaySelected: () -> Unit,
    onVoucherSelected: () -> Unit, onTopupSelected: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var amount: String by remember { mutableStateOf("1000") }
    LaunchedEffect(Unit) {

        /*
           lifecycleScope.launch(Dispatchers.IO) {
            printLastTransactionInQueue()
            DoSettleReverse().sendTransactionInQueue()
        }
         */
    }
    LaunchedEffect(uiState.purchase) {
        if (uiState.purchase)
            onPurchaseSelected(amount)
    }
    LaunchedEffect(uiState.balance) {
        if (uiState.balance)
            onBalanceSelected()
    }
    LaunchedEffect(uiState.billPay) {
        if (uiState.billPay)
            onBillPaySelected()
    }
    LaunchedEffect(uiState.voucher) {
        if (uiState.voucher)
            onVoucherSelected()
    }
    LaunchedEffect(uiState.topup) {
        if (uiState.topup)
            onTopupSelected()
    }
    ConstraintLayout(
        ConstraintSet {
            val hambergerButton = createRefFor("hambergerButton")
            val pspLogo = createRefFor("pspLogo")
//            val merchantName = createRefFor("merchantName")
//            val terminalIdTitle = createRefFor("terminalIdTitle")
//            val terminalId = createRefFor("terminalId")
            val purchaseBox = createRefFor("purchaseBox")
            val billPayBox = createRefFor("billPayBox")
            val balanceBox = createRefFor("balanceBox")
            val voucherBox = createRefFor("voucherBox")
            val topupBox = createRefFor("topupBox")
//            val message = createRefFor("message")

            constrain(hambergerButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, 20.dp)
            }
            constrain(pspLogo) {
                top.linkTo(hambergerButton.top)
                bottom.linkTo(hambergerButton.bottom)
                start.linkTo(parent.start, 20.dp)
            }
//            constrain(merchantName) {
//                top.linkTo(pspLogo.bottom)
//                start.linkTo(pspLogo.start)
//            }
//            constrain(terminalIdTitle) {
//                top.linkTo(merchantName.bottom)
//                start.linkTo(pspLogo.start)
//            }
//            constrain(terminalId) {
//                top.linkTo(terminalIdTitle.top)
//                bottom.linkTo(terminalIdTitle.bottom)
//                start.linkTo(terminalIdTitle.end)
//            }
            constrain(purchaseBox) {
                top.linkTo(pspLogo.bottom, 40.dp)
                start.linkTo(pspLogo.start)
                end.linkTo(hambergerButton.end)
                width = Dimension.fillToConstraints
            }
            constrain(billPayBox) {
                top.linkTo(purchaseBox.bottom, 20.dp)
                start.linkTo(pspLogo.start)
                end.linkTo(balanceBox.start)
                width = Dimension.fillToConstraints
            }
            constrain(balanceBox) {
                top.linkTo(billPayBox.top)
                bottom.linkTo(billPayBox.bottom)
                end.linkTo(hambergerButton.end)
                start.linkTo(billPayBox.end)
                width = Dimension.fillToConstraints
            }
            constrain(topupBox) {
                top.linkTo(billPayBox.bottom, 12.dp)
                start.linkTo(pspLogo.start)
                end.linkTo(voucherBox.start)
                width = Dimension.fillToConstraints
            }
            constrain(voucherBox) {
                top.linkTo(topupBox.top)
                bottom.linkTo(topupBox.bottom)
                end.linkTo(hambergerButton.end)
                start.linkTo(topupBox.end)
                width = Dimension.fillToConstraints

            }
//            constrain(message) {
//                bottom.linkTo(parent.bottom, 20.dp)
//                end.linkTo(parent.end)
//                start.linkTo(parent.start)
//                width = Dimension.fillToConstraints
//
//            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        HambergerButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .layoutId("hambergerButton")
        ) {
            onSettingsClicked()
        }
        Image(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(45.dp)
                .width(140.dp)
                .layoutId("pspLogo"),
            painter = painterResource(id = R.drawable.padakht_novin_logo),
            contentDescription = ""
        )
//        Text(text = "فروشگاه امیری", modifier = Modifier.layoutId("merchantName"), color = White100)
//        Text(
//            text = "شماره پایانه",
//            modifier = Modifier.layoutId("terminalIdTitle"),
//            color = Gray100
//        )
        // Text(text = "123456789", modifier = Modifier.layoutId("terminalId"), color = Gray100)
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .border(
//                    width = 2.dp,
//                    brush = Brush.verticalGradient(
//                        colors = listOf(White200, White200),
//                    ), shape = RoundedCornerShape(16.dp)
//                )
                .background(color = Black200, shape = RoundedCornerShape(16.dp))
                .layoutId("purchaseBox")
        ) {
            PriceTextInput(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp)
                    .height(HEIGHT_TEXT_INPUT)
                    .fillMaxWidth(),
                title = "مبلغ خرید را وارد کنید",
                trailerTitle = stringResource(id = R.string.currency), value = amount
            ) {
                amount = it
            }
            MainButton(
                title = "تایید", modifier = Modifier
                    .padding(top = 18.dp)
                    .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()

            ) {
                println("yyyyyyyyyyyyyyyy")
                if (amount.isNotEmpty()) {
                    // onPurchaseSelected(amount)
                    viewModel.purchase()
                } else {
                    // TODO:  az shaqaye
                }
            }
        }

        MenuItem(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                   viewModel.billPay()
                }
                .fillMaxWidth(1f)
                .layoutId("billPayBox"), backgroundImageId = R.drawable.aaaa,
            iconId = R.drawable.ic_bill_pay, title = "پرداخت قبض"
        )
        MenuItem(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                    viewModel.balance()
                }
                .fillMaxWidth(1f)
                .layoutId("balanceBox"), backgroundImageId = R.drawable.balance_container,
            iconId = R.drawable.ic_balance, title = "مانده حساب"
        )


        MenuItem(
            modifier = Modifier
                .clickable {
                    viewModel.topup()
                }
                .fillMaxWidth(1f)
                .layoutId("topupBox"), backgroundImageId = R.drawable.ddd,
            iconId = R.drawable.ic_topup, title = "خرید شارژ مستقیم"
        )
        MenuItem(
            modifier = Modifier
                .clickable {
                    viewModel.voucher()
                }
                .fillMaxWidth(1f)
                .layoutId("voucherBox"), backgroundImageId = R.drawable.cccc,
            iconId = R.drawable.ic_voucher, title = "خرید کد شارژ"
        )
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(White100)
//                .layoutId("message").padding(top = 3.dp, bottom = 3.dp),
//            text = "سه شنبه های بدون دود",
//            textAlign = TextAlign.Center, color = Pink
//        )
    }
}

@Composable
fun TriangleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val squareSize = 100.dp
    val triangleSize = squareSize / 10

    Box(
        modifier = modifier
            .size(squareSize)
            .clickable { onClick() }
            .background(color = Color.Blue)
            .clipToBounds()
    ) {
        Canvas(
            modifier = Modifier
                .size(triangleSize, triangleSize)
                .align(Alignment.CenterStart)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color = Color.White)
        }
    }
}

//@Composable
//@Preview
//fun PriceButtonPreview() {
//    TotanPayTheme {
//        TriangleButton(
//            modifier = Modifier
//                .size(2.dp)
//        ){
//
//        }
//    }
//}
@Composable
@Preview
fun MainScreenPreview() {
    TotanPayTheme {
        MainScreen(
            hiltViewModel(),
            onPurchaseSelected = {},
            onBalanceSelected = {},
            onVoucherSelected = {},
            onTopupSelected = {},
            onBillPaySelected = {}) {}
    }
}
