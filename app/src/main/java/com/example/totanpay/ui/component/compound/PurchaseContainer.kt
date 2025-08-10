package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.ui.component.PurchasePriceTextInput
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun PurchaseContainer(textInputModifier: Modifier,
    modifier: Modifier,
    amountHasError: Boolean,
    amount: String,
    onChangeAmountVale: (String) -> Unit,
    onPurchaseSelected: (String, Boolean) -> Unit,
    onChangeAmountHaseError: (Boolean) -> Unit,
    onShowToastChangeValue: (Boolean) -> Unit
) {
    var purchaseIdIsEnabled by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    ConstraintLayout(
        ConstraintSet {
            val purchasePriceTextInput = createRefFor("purchasePriceTextInput")
            val switchContainer = createRefFor("switchContainer")
            val confirm = createRefFor("confirm")

            constrain(purchasePriceTextInput) {
                top.linkTo(parent.top, 10.dp)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints
            }

            constrain(switchContainer) {
                top.linkTo(purchasePriceTextInput.bottom)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints

            }
            constrain(confirm) {
                top.linkTo(switchContainer.bottom)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints

            }
        }, modifier = modifier
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(bottom = 10.dp)
    ) {
        PurchasePriceTextInput(textInputModifier=textInputModifier,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 0.dp)
                .fillMaxWidth()
                .layoutId("purchasePriceTextInput"),
            hasError = amountHasError,
            errorMessage = stringResource(R.string.enter_purchase_amount),
            title = stringResource(R.string.enter_purchase_amount),
            trailerTitle = stringResource(id = R.string.currency),
            value = amount,
            onDone = {
                keyboard?.hide()
            }
        ) {
            onShowToastChangeValue(false)
            onChangeAmountVale(it)
        }
        Row(
            modifier = Modifier
                .padding(start = 18.dp, end = 18.dp)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .layoutId("switchContainer"),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = purchaseIdIsEnabled,
                onCheckedChange = {
                    purchaseIdIsEnabled = it
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Green50
                ),
                modifier = Modifier
                    .graphicsLayer(scaleX = 1f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.purchase_with_id),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium
            )
        }
        MainButton(
            Modifier
                .mainButtonModifier(isSmall = isSmall(context = LocalContext.current))
                .layoutId("confirm")
        ) {
            keyboard?.hide()
            if (amount.isNotEmpty()) {
                if (!purchaseIdIsEnabled) {
                    onPurchaseSelected(amount, false)
                } else {
                    onPurchaseSelected(amount, true)
                }
            } else {
                onShowToastChangeValue(true)
                onChangeAmountHaseError(true)
            }
        }
    }
}

@Composable
@Preview
fun PurchaseContainerPreview() {
    TotanPayTheme {
        PurchaseContainer(textInputModifier=Modifier,
            modifier = Modifier.fillMaxWidth(),
            amountHasError = false,
            amount = "1000",
            onChangeAmountVale = {

            },
            onPurchaseSelected = { amount, purchaseId ->

            },
            onShowToastChangeValue = {

            },
            onChangeAmountHaseError = {
            })
    }
}