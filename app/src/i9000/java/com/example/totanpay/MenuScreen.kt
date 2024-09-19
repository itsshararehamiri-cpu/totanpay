package com.example.totanpay

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.multidex.BuildConfig
import com.example.totanpay.data.repository.datasource.formatAmount
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.feature.bill.BillPaymentReceiptContent
import com.example.totanpay.feature.purchase.PurchaseIdBottomDialog
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.feature.purchase.UnSuccessReceiptContent
import com.example.totanpay.feature.settings.EnterPasswordBottomDialog
import com.example.totanpay.feature.topup.TopUpReceiptContent
import com.example.totanpay.feature.voucher.VoucherReceiptContent
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.HamburgerButton
import com.example.totanpay.ui.component.compound.MenuItem
import com.example.totanpay.ui.component.compound.PurchaseContainer
import com.example.totanpay.ui.component.dialog.SettingsDataTimeDialog
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    onPurchaseSelected: (String, String?) -> Unit,
    onBalanceSelected: () -> Unit,
    onBillPaySelected: () -> Unit,
    onVoucherSelected: () -> Unit,
    onTopUpSelected: () -> Unit,
    onSettingsClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember { mutableStateOf(null) }
    var purchaseIdValue: String? by remember { mutableStateOf(null) }
    var amountValue: String by remember { mutableStateOf("") }
    BackHandler {
        viewModel.showGetExitPasswordDialog()
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
            viewModel.print(bitmap = receiptBitmap!!, context = context, onSuccess = {}, onFailed = {})
    }
    LaunchedEffect(uiState.purchase) {
        if (uiState.purchase) {
            onPurchaseSelected(amountValue, purchaseIdValue)
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
    LaunchedEffect(uiState.isExit) {
        if (uiState.isExit)
            (context as MainActivity).finish()
    }
    MenuContent(uiState = uiState,
        onPurchaseSelected = { amount, purchaseId ->
            amountValue = amount
            purchaseIdValue = purchaseId
            viewModel.purchase(context)
        },
        onBalanceSelected = {
            viewModel.balance(context)
        },
        onBillPaySelected = { viewModel.billPay(context) },
        onVoucherSelected = { viewModel.voucher(context) },
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
        },  hideExitPasswordDialog = { viewModel.hideGetExitPasswordDialog() },validateExitPassword = {
            viewModel.checkExistPassword(it)
        },
        hideMessageNeedToSetApportionment={
            viewModel.hideMessageNeedToSetApportionment()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuContent(
    uiState: MenuUiState,
    onPurchaseSelected: (String, String) -> Unit,
    onBalanceSelected: () -> Unit,
    onBillPaySelected: () -> Unit,
    onVoucherSelected: () -> Unit,
    onTopUpSelected: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHideConfigurationIsNotCompletedMessage: () -> Unit,
    hideInternetIsNotAvailableMessage: () -> Unit,
    hideSwitchIsNotAvailableMessage: () -> Unit,
    validateExitPassword: (String) -> Unit,
    hideExitPasswordDialog: () -> Unit,
    hideMessageNeedToSetApportionment:()->Unit

    ) {
    var amount: String by remember { mutableStateOf("") }
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var showAmountIsNotCorrectRangeToast by remember { mutableStateOf(false) }
    var showAmountMustBeGreaterThanToast by remember { mutableStateOf(false) }

    var showPurchaseIdIsNotEnteredToast by remember { mutableStateOf(false) }
    var showPurchaseIdBottomDialog by remember { mutableStateOf(false) }
    var purchaseId: String by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(showPurchaseIdBottomDialog) {
        if (showPurchaseIdBottomDialog) {
            keyboard?.hide()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            ConstraintSet {
                val hamburgerButton = createRefFor("hamburgerButton")
                val pspLogo = createRefFor("pspLogo")
                val merchantName = createRefFor("merchantName")
                val terminalIdTitle = createRefFor("terminalIdTitle")
                val terminalId = createRefFor("terminalId")
                val purchaseBox = createRefFor("purchaseBox")
                val billPayBox = createRefFor("billPayBox")
                val balanceBox = createRefFor("balanceBox")
                val voucherBox = createRefFor("voucherBox")
                val topUpBox = createRefFor("topUpBox")
                val progress = createRefFor("progress")
                constrain(hamburgerButton) {
                    top.linkTo(parent.top, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                }
                constrain(pspLogo) {
                    top.linkTo(hamburgerButton.top)
                    bottom.linkTo(hamburgerButton.bottom)
                    start.linkTo(parent.start, 20.dp)
                }
                constrain(merchantName) {
                    top.linkTo(pspLogo.bottom, 20.dp)
                    start.linkTo(pspLogo.start, 8.dp)
                }
                constrain(terminalIdTitle) {
                    top.linkTo(merchantName.bottom, 6.dp)
                    start.linkTo(merchantName.start)
                }
                constrain(terminalId) {
                    top.linkTo(terminalIdTitle.top)
                    bottom.linkTo(terminalIdTitle.bottom)
                    start.linkTo(terminalIdTitle.end, 10.dp)
                }
                constrain(progress) {
                    top.linkTo(pspLogo.bottom, 12.dp)
                    start.linkTo(pspLogo.start, 8.dp)
                }
                constrain(purchaseBox) {
                    top.linkTo(terminalIdTitle.bottom, 12.dp)
                    start.linkTo(pspLogo.start)
                    end.linkTo(hamburgerButton.end)
                    width = Dimension.fillToConstraints
                }
                constrain(billPayBox) {
                    top.linkTo(purchaseBox.bottom, 12.dp)
                    start.linkTo(pspLogo.start)
                    end.linkTo(balanceBox.start)
                    width = Dimension.fillToConstraints
                }
                constrain(balanceBox) {
                    top.linkTo(billPayBox.top)
                    bottom.linkTo(billPayBox.bottom)
                    end.linkTo(hamburgerButton.end)
                    start.linkTo(billPayBox.end)
                    width = Dimension.fillToConstraints
                }
                constrain(topUpBox) {
                    top.linkTo(billPayBox.bottom, 10.dp)
                    start.linkTo(billPayBox.start)
                    end.linkTo(billPayBox.end)
                    width = Dimension.fillToConstraints
                }
                constrain(voucherBox) {
                    top.linkTo(topUpBox.top)
                    bottom.linkTo(topUpBox.bottom)
                    end.linkTo(balanceBox.end)
                    start.linkTo(balanceBox.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                contentDescription = "")
            Text(
                text = uiState.merchantName,
                modifier = Modifier.layoutId("merchantName"),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium .copy(fontSize = 13.sp)
            )
            if (uiState.merchantName.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.terminal_id),
                    modifier = Modifier.layoutId("terminalIdTitle"),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall .copy(fontSize = 13.sp)
                )
                Text(
                    text = uiState.terminalId,
                    modifier = Modifier.layoutId("terminalId"),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (uiState.showProgress)
                CircularProgressIndicator(
                    modifier = Modifier
                        .layoutId("progress")
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("purchaseBox")
            ) {
                PurchaseContainer(modifier = Modifier.fillMaxWidth(),
                    amountHasError = amountHasError,
                    amount = amount,
                    onChangeAmountVale = {
                        if(it.isNotEmpty())
                        {
                            if(it.toEnglishNumber().toLong()<=uiState.maximumAmountForPurchaseTransaction.toLong()){
                                amount = it
                            }
                            else{
                                showAmountIsNotCorrectRangeToast=true
                            }
                        }
                        else{
                            amount = it.toEnglishNumber()
                        }
                    },
                    onPurchaseSelected = { amount, purchaseId ->
                        showAmountMustBeGreaterThanToast=false
                        if(amount.isNullOrEmpty())
                        {
                            if (purchaseId)
                                showPurchaseIdBottomDialog=true
                            else  onPurchaseSelected(amount, "")
                        }
                        else{
                            if(amount.toEnglishNumber().toLong()<1000)
                            {
                                showAmountMustBeGreaterThanToast=true
                            }
                            else{
                                if (purchaseId)
                                    showPurchaseIdBottomDialog=true
                                else  onPurchaseSelected(amount, "")
                            }
                        }
                    },
                    onShowToastChangeValue = {
                        showToast = it
                    },
                    onChangeAmountHaseError = {
                        amountHasError = it
                    })
                if (showToast) {
                    ShowToast(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        message = stringResource(R.string.amount_not_entered)
                    ) {
                        showToast = false
                    }
                }
                if (showAmountIsNotCorrectRangeToast) {
                    ShowToast(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        message = stringResource(R.string.amount_should_be_greater_than,"2000000000".formatAmount())
                    ) {
                        showAmountIsNotCorrectRangeToast = false
                    }
                }
                if (showAmountMustBeGreaterThanToast) {
                    ShowToast(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        message = stringResource(R.string.amount_should_be_smaller_than,"1000".formatAmount())
                    ) {
                        showAmountMustBeGreaterThanToast = false
                    }
                }
                if (showPurchaseIdIsNotEnteredToast) {
                    ShowToast(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        message = stringResource(R.string.purchase_id_not_entered)
                    ) {
                        showPurchaseIdIsNotEnteredToast = false
                    }
                }
            }
            MenuItem(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        onBillPaySelected()
                    }
                    .layoutId("billPayBox"),
                backgroundImageId = R.drawable.main_item_background_to_left,
                iconId = R.drawable.ic_bill,
                backgroundIconId = R.drawable.background_bill,
                title = TransactionType.BILL_PAY.title
            )
            MenuItem(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        onBalanceSelected()
                    }
                    .layoutId("balanceBox"),
                backgroundImageId = R.drawable.main_item_background_to_right,
                iconId = R.drawable.ic_balance,
                backgroundIconId = R.drawable.background_balance,
                title = TransactionType.BALANCE.title
            )


            MenuItem(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        onTopUpSelected()
                    }
                    .layoutId("topUpBox"),
                backgroundImageId = R.drawable.main_item_background_to_left,
                iconId = R.drawable.ic_topup,
                backgroundIconId = R.drawable.background_topup,
                title = TransactionType.TOPUP.title
            )
            MenuItem(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        onVoucherSelected()
                    }
                    .layoutId("voucherBox"),
                backgroundImageId = R.drawable.main_item_background_to_right,
                iconId = R.drawable.ic_voucher,
                backgroundIconId = R.drawable.background_voucher,
                title = TransactionType.VOUCHER.title
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
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (showPurchaseIdBottomDialog) {
            ModalBottomSheet(
                onDismissRequest = {    purchaseId = ""
                    showPurchaseIdBottomDialog = false },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
            PurchaseIdBottomDialog(
                Modifier
                    .fillMaxWidth(),
                onConfirmButtonClicked = {
                    purchaseId = it
                    showPurchaseIdBottomDialog = false
                    if (amount.isNotEmpty() && purchaseId.isNotEmpty()) {
                        onPurchaseSelected(amount.toEnglishNumber(), purchaseId.toEnglishNumber())
                    }
                },
                onCancelButtonClicked = {
                    purchaseId = ""
                    showPurchaseIdBottomDialog = false
                })
            }
        }
        if (uiState.showExitPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = uiState.existPasswordError,
                onConfirmButtonClicked = {
                    validateExitPassword(it)
                },
                onCancelButtonClicked = {
                    hideExitPasswordDialog()
                })
        }
        if (uiState.showMessageNeedToSetApportionment) {
            SettingsDataTimeDialog(titleMessage =  "لطفا به قسمت مدیریت حساب ها بروید و سهم هر شماره حساب را تعیین کنید.", onDismiss = {
            }, onConfirmButtonClicked = {
                hideMessageNeedToSetApportionment()
            })
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    TotanPayTheme {
        MenuContent(
            uiState = MenuUiState(),
            onPurchaseSelected = { _, _ -> },
            onBalanceSelected = {},
            onVoucherSelected = {},
            onTopUpSelected = {},
            onBillPaySelected = {},
            onSettingsClicked = {},
            onHideConfigurationIsNotCompletedMessage = {},
            hideInternetIsNotAvailableMessage = {},
            hideSwitchIsNotAvailableMessage = {}, hideExitPasswordDialog = {}, validateExitPassword = {}, hideMessageNeedToSetApportionment = {})
    }
}
