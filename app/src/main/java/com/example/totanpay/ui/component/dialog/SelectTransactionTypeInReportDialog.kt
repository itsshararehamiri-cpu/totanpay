package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.component.TransactionTypeCheckbox
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTransactionTypeInReport(
    onCancelButtonClicked: () -> Unit, confirmTransactionType: (String) -> Unit
) {
    var purchaseIsSelected by remember { mutableStateOf(true) }
    var billPayIsSelected by remember { mutableStateOf(false) }
    var voucherIsSelected by remember { mutableStateOf(false) }
    var topUpIsSelected by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        if (keyEvent.key == Key.Enter) {
                            val transactionType = JSONObject()
                            transactionType.apply {
                                put("purchaseType", if (purchaseIsSelected) "has" else "dontHas")
                                put("billPayType", if (billPayIsSelected) "has" else "dontHas")
                                put("voucherType", if (voucherIsSelected) "has" else "dontHas")
                                put("topupType", if (topUpIsSelected) "has" else "dontHas")
                            }
                            confirmTransactionType(
                                transactionType.toString()

                            )
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
        ) {
            ConstraintLayout(
                ConstraintSet {
                    val transactionTypeTitle = createRefFor("transactionTypeTitle")
                    val purchase = createRefFor("purchase")
                    val billPay = createRefFor("billPay")
                    val voucher = createRefFor("voucher")
                    val topup = createRefFor("topup")
                    constrain(transactionTypeTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    constrain(purchase) {
                        top.linkTo(transactionTypeTitle.bottom)
                        start.linkTo(transactionTypeTitle.start)
                    }
                    constrain(topup) {
                        top.linkTo(purchase.top)
                        bottom.linkTo(purchase.bottom)
                        end.linkTo(parent.end, 16.dp)
                    }
                    constrain(voucher) {
                        top.linkTo(purchase.bottom)
                        start.linkTo(purchase.start)
                    }
                    constrain(billPay) {
                        top.linkTo(voucher.top)
                        bottom.linkTo(voucher.bottom)
                        start.linkTo(topup.start)
                    }
                }, modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {

                Text(
                    text = stringResource(R.string.transaction_type), color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .layoutId("transactionTypeTitle"),
                    style = MaterialTheme.typography.bodyLarge
                )
                val context= LocalContext.current
                TransactionTypeCheckbox(modifier = Modifier.layoutId("purchase"),
                    title = context.getString(TransactionType.PURCHASE.title),
                    isChecked = purchaseIsSelected,
                    onCheckedChange = {
                        purchaseIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("voucher"),
                    title = context.getString(TransactionType.VOUCHER.title),
                    isChecked = voucherIsSelected,
                    onCheckedChange = {
                        voucherIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("topup"),
                    title = context.getString(TransactionType.TOPUP.title),
                    isChecked = topUpIsSelected,
                    onCheckedChange = {
                        topUpIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("billPay"),
                    title = context.getString(TransactionType.BILL_PAY.title),
                    isChecked = billPayIsSelected,
                    onCheckedChange = {
                        billPayIsSelected = it
                    })
            }
        }
    }

}