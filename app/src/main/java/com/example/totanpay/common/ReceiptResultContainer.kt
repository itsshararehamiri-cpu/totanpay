package com.example.totanpay.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.PrintButtonModifier
import com.example.totanpay.ui.ReturnBackToMainButtonModifier
import com.example.totanpay.ui.SmallPrintButtonModifier
import com.example.totanpay.ui.SmallReturnBackToMainButtonModifier
import com.example.totanpay.ui.component.button.PrintButton
import com.example.totanpay.ui.component.button.ReturnBackToMainButton
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ReceiptResultContainer(
    printTitle: String, onPrintButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    ConstraintLayout(
//        if (isSmall(context))
//        ConstraintSet {
//            val receipt = createRefFor("receipt")
//            val printButton = createRefFor("printButton")
//            val returnBackButton = createRefFor("returnBackButton")
//            constrain(receipt) {
//                top.linkTo(parent.top, 5.dp)
//                end.linkTo(parent.end, 0.dp)
//                start.linkTo(parent.start, 0.dp)
//                bottom.linkTo(printButton.top)
//
//            }
//            constrain(returnBackButton) {
//                bottom.linkTo(parent.bottom)
//                end.linkTo(parent.end, 0.dp)
//                start.linkTo(printButton.end, 0.dp)
//                width = Dimension.fillToConstraints
//            }
//            constrain(printButton) {
//                bottom.linkTo(returnBackButton.bottom)
//                top.linkTo(returnBackButton.top)
//                end.linkTo(receipt.start, 0.dp)
//                start.linkTo(parent.start, 0.dp)
//                width = Dimension.fillToConstraints
//                height=Dimension.fillToConstraints
//
//            }
//        }
   // else {
        ConstraintSet {
            val receipt = createRefFor("receipt")
            val printButton = createRefFor("printButton")
           val returnBackButton = createRefFor("returnBackButton")
            constrain(receipt) {
                top.linkTo(parent.top, 20.dp)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                bottom.linkTo(printButton.top)

            }
            constrain(returnBackButton) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints
            }
            constrain(printButton) {
                bottom.linkTo(returnBackButton.top, 5.dp)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints
                height=if(isSmall(context))Dimension.value(0.dp) else Dimension.wrapContent

            }
     //   }
    }, modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
    ) {
        content()

        PrintButton(
            title = printTitle,
            modifier =if(isSmall(context))
            SmallPrintButtonModifier .layoutId("printButton") else PrintButtonModifier
                .layoutId("printButton")
        ) {
            onPrintButtonClicked()
        }
        ReturnBackToMainButton(isSmall = isSmall(context),
            modifier =
            if(isSmall(context))
                SmallReturnBackToMainButtonModifier.layoutId("returnBackButton") else ReturnBackToMainButtonModifier

                .layoutId("returnBackButton")

        ) {
            onBackButtonClicked()
        }
    }
}

@Composable
@Preview
fun ReceiptResultContainerPreview() {
    TotanPayTheme {
        ReceiptResultContainer(
            printTitle = stringResource(R.string.print_customer_receipt),
            onBackButtonClicked = {
            },
            onPrintButtonClicked = {
            }) {
            Column {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text(stringResource(R.string.print_customer_receipt))
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text(stringResource(R.string.print_customer_receipt))
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text(stringResource(R.string.print_customer_receipt))
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text(stringResource(R.string.print_customer_receipt))
                }
            }
        }
    }
}