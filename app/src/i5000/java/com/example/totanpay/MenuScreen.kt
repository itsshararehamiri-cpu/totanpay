package com.example.totanpay


import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.multidex.BuildConfig
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.bill.BillPaymentReceiptContent
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.feature.purchase.UnSuccessReceiptContent
import com.example.totanpay.feature.topup.TopUpReceiptContent
import com.example.totanpay.feature.voucher.VoucherReceiptContent
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.HamburgerButton
import com.example.totanpay.ui.component.compound.SmallMenuItem
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onPurchaseSelected: () -> Unit,
    onBalanceSelected: () -> Unit,
    onBillPaySelected: () -> Unit,
    onVoucherSelected: () -> Unit,
    onTopUpSelected: () -> Unit,
    onSettingsClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val device = LocalDeviceManager.current
    var receiptBitmap: Bitmap? by remember { mutableStateOf(null) }
    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(context)
    }
    if (uiState.lastTransactionIsNotPrinted != null) {
        ReceiptUi(content = {
            if (uiState.lastTransactionIsNotPrinted!!.responseCode == "00") {
                if (uiState.lastTransactionIsNotPrinted!!.transactionType == TransactionType.PURCHASE.title) {
                    ReceiptContent(true, uiState.lastTransactionIsNotPrinted!!, true)
                }
                if (uiState.lastTransactionIsNotPrinted!!.transactionType == TransactionType.VOUCHER.title) {
                    VoucherReceiptContent(true, uiState.lastTransactionIsNotPrinted, true)
                }
                if (uiState.lastTransactionIsNotPrinted!!.transactionType == TransactionType.TOPUP.title) {
                    TopUpReceiptContent(true, uiState.lastTransactionIsNotPrinted, true)
                }
                if (uiState.lastTransactionIsNotPrinted!!.transactionType == TransactionType.BILL_PAY.title) {
                    BillPaymentReceiptContent(
                        isPaperReceipt = true,
                        result = uiState.lastTransactionIsNotPrinted,
                        true
                    )
                }
            } else {
                UnSuccessReceiptContent(true, uiState.lastTransactionIsNotPrinted)
            }
        }) {
            receiptBitmap = it
        }
    }
    LaunchedEffect(receiptBitmap) {
        if (receiptBitmap != null)
            device.print(bitmap = receiptBitmap!!, context = context, onSuccess = {}, onFailed = {})
    }
    LaunchedEffect(uiState.purchase) {
        if (uiState.purchase) {
            onPurchaseSelected()
        }
    }
    LaunchedEffect(uiState.balance) {
        if (uiState.balance) {
            onBalanceSelected()
        }
    }
    LaunchedEffect(uiState.billPay) {
        if (uiState.billPay) {
            onBillPaySelected()
        }
    }
    LaunchedEffect(uiState.voucher) {
        if (uiState.voucher) {
            onVoucherSelected()
        }
    }
    LaunchedEffect(uiState.topUp) {
        if (uiState.topUp)
            onTopUpSelected()
    }
    MenuContent(uiState = uiState,
        onPurchaseSelected = {
            viewModel.purchase(context)
        },
        onBalanceSelected = {
            viewModel.balance(context)
        },
        onBillPaySelected = { viewModel.billPay(context) },
        onTopUpSelected = {
            viewModel.topUp(context)
        },
        onSettingsClicked = {
            onSettingsClicked()
        },
        onHideConfigurationIsNotCompletedMessage = {
            viewModel.hideConfigurationIsNotCompletedMessage()
        },
        hideInternetIsNotAvailableMessage = {
            viewModel.hideInternetIsNotAvailableMessage()
        },
        hideSwitchIsNotAvailableMessage = {
            viewModel.hideSwitchIsNotAvailableMessage()
        })
}

@Composable
fun MenuContent(
    uiState: MenuUiState,
    onPurchaseSelected: () -> Unit,
    onBalanceSelected: () -> Unit,
    onBillPaySelected: () -> Unit,
    onTopUpSelected: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHideConfigurationIsNotCompletedMessage: () -> Unit,
    hideInternetIsNotAvailableMessage: () -> Unit,
    hideSwitchIsNotAvailableMessage: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            ConstraintSet {
                val hamburgerButton = createRefFor("hamburgerButton")
                val pspLogo = createRefFor("pspLogo")
                val purchaseBox = createRefFor("purchaseBox")
                val billPayBox = createRefFor("billPayBox")
                val balanceBox=createRefFor("balanceBox")
                val charge=createRefFor("charge")
                constrain(hamburgerButton) {
                    top.linkTo(parent.top, 15.dp)
                    end.linkTo(parent.end, 8.dp)
                }
                constrain(pspLogo) {
                    top.linkTo(hamburgerButton.top)
                    bottom.linkTo(hamburgerButton.bottom)
                    start.linkTo(parent.start, 8.dp)
                }
                constrain(purchaseBox) {
                    top.linkTo(pspLogo.bottom, 10.dp)
                    end.linkTo(hamburgerButton.end)
                    start.linkTo(billPayBox.end, 4.dp)
                    bottom.linkTo(charge.top)
                    width = Dimension.fillToConstraints
                    height=Dimension.fillToConstraints
                }
                constrain(charge) {
                    top.linkTo(purchaseBox.bottom,8.dp)
                    bottom.linkTo(parent.bottom,10.dp)
                    start.linkTo(purchaseBox.start)
                    end.linkTo(purchaseBox.end)
                    width = Dimension.fillToConstraints
                    height=Dimension.fillToConstraints
                }
                constrain(billPayBox) {
                    top.linkTo(purchaseBox.top)
                    end.linkTo(purchaseBox.start,4.dp)
                    start.linkTo(pspLogo.start)
                    bottom.linkTo(purchaseBox.bottom)
                    width = Dimension.fillToConstraints
                    height=Dimension.fillToConstraints
                }

                constrain(balanceBox) {
                    top.linkTo(charge.top)
                    bottom.linkTo(charge.bottom)
                    start.linkTo(billPayBox.start)
                    end.linkTo(billPayBox.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            HamburgerButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .layoutId("hamburgerButton")
            ) {
                onSettingsClicked()
            }
            Image(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(40.dp)
                    .width(140.dp)
                    .layoutId("pspLogo"),
                painter = painterResource(
                    id = if (BuildConfig.FLAVOR == "pn") R.drawable.padakht_novin_logo else R.drawable.fanava_logo
                ),
                contentDescription = ""
            )
            val modifier = Modifier.fillMaxWidth()

            SmallMenuItem(
                modifier =
                modifier
                    .layoutId("purchaseBox")
                    .clickable {
                        onPurchaseSelected()
                    },
                backgroundImageId = R.drawable.ic_i5000_purchase,
                iconId = R.drawable.ic_i5000_purchase,
                backgroundIconId = R.drawable.ic_i5000_purchase,
                title = TransactionType.PURCHASE.title
            )

            SmallMenuItem(
                modifier = modifier
                    .layoutId("billPayBox")
                    .clickable {
                        onBillPaySelected()
                    },
                backgroundImageId = R.drawable.main_item_background_to_left,
                iconId = R.drawable.ic_i5000_bill,
                backgroundIconId = R.drawable.ic_i5000_purchase,
                title = TransactionType.BILL_PAY.title
            )



            SmallMenuItem(
                modifier = modifier
                    .layoutId("balanceBox")
                    .clickable {
                        onBalanceSelected()
                    },
                backgroundImageId = R.drawable.main_item_background_to_right,
                iconId = R.drawable.ic_i5000_balance,
                backgroundIconId = R.drawable.ic_i5000_purchase,
                title = TransactionType.BALANCE.title
            )




            SmallMenuItem(
                modifier = modifier
                    .layoutId("charge")
                    .clickable {
                        onTopUpSelected()
                    },
                backgroundImageId = R.drawable.main_item_background_to_left,
                iconId = R.drawable.ic_i5000_charge,
                backgroundIconId = R.drawable.ic_i5000_purchase,
                title = TransactionType.CHARGE.title
            )

    }
    if (uiState.showInternetIsNotAvailableMessage) {
        ShowToast(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(R.string.there_is_no_transaction_to_perform_please_check_network)
        ) {
            hideInternetIsNotAvailableMessage()
        }
    }
    if (uiState.showSwitchIsNotAvailableMessage) {
        ShowToast(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(R.string.switch_is_not_accessible)
        ) {
            hideSwitchIsNotAvailableMessage()
        }
    }
    if (uiState.configurationIsNotCompletedMessage.isNotEmpty()) {
        ShowToast(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = stringResource(R.string.configuration_not_completed)
        ) {
            onHideConfigurationIsNotCompletedMessage()
        }
    }
    if (uiState.showBatteryStatusMessage) {
        ShowToast(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = "امکان چاپ وجود ندارد"
        ) {

        }
    }
}
}

@Composable
@Preview
fun MainScreenPreview() {
    TotanPayTheme {
        MenuContent(
            uiState = MenuUiState(),
            onPurchaseSelected = {  },
            onBalanceSelected = {},
            onTopUpSelected = {},
            onBillPaySelected = {},
            onSettingsClicked = {},
            onHideConfigurationIsNotCompletedMessage = {},
            hideInternetIsNotAvailableMessage = {},
            hideSwitchIsNotAvailableMessage = {})
    }
}
