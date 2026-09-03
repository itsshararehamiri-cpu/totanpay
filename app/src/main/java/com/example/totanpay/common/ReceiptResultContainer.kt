package com.example.totanpay.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.SmallPrintButtonModifier
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.PrintButton
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT

@Composable
fun ReceiptResultContainer(
    errorInPrint: String,
    showCustomerPrintButton: Boolean,
    showMerchantPrintButton: Boolean,
    printTitle: String, onCustomerPrintButtonClicked: () -> Unit,
    onMerchantPrintButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    clearPrintErrorMessage: () -> Unit,
    content: @Composable () -> Unit
) {
    var firstClicked by remember { mutableStateOf(true) }
    var secondClicked by remember { mutableStateOf(true) }


    var showErrorToast by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(errorInPrint) {
        if (errorInPrint.isNotEmpty()) {
            showErrorToast = true
        }
    }
    Box(Modifier.fillMaxSize()) {
        ConstraintLayout(
            ConstraintSet {
                val receipt = createRefFor("receipt")
                val printButtonsArea = createRefFor("printButtonsArea")

                constrain(receipt) {
                    top.linkTo(parent.top, 20.dp)
                    end.linkTo(parent.end, 0.dp)
                    start.linkTo(parent.start, 0.dp)
                    bottom.linkTo(printButtonsArea.top)
                }
                constrain(printButtonsArea) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
            }, modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            content()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("printButtonsArea")
            ) {
                if (showCustomerPrintButton)
                    PrintButton(
                        enabled = firstClicked,
                        title = printTitle,
                        modifier = if (isSmall(context))
                            SmallPrintButtonModifier.weight(1f) else Modifier
                            .padding(end = 1.dp)
                            .height(BUTTON_HEIGHT)
                            .weight(1f)
                    ) {
                        firstClicked=false
                        onCustomerPrintButtonClicked()
                    }

                if (showMerchantPrintButton)
                    PrintButton(
                        enabled = secondClicked,
                        title = stringResource(R.string.merchant_receipt),
                        modifier = if (isSmall(context))
                            SmallPrintButtonModifier.weight(1f) else Modifier
                            .padding(start = 1.dp)
                            .height(BUTTON_HEIGHT)
                            .weight(1f)
                    ) {
                        onMerchantPrintButtonClicked()
                        secondClicked=false
                    }

            }
        }
        if (showErrorToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter), message = errorInPrint
            ) {
                showErrorToast = false
                clearPrintErrorMessage()
            }
        }
    }
}
