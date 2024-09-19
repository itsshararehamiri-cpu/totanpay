package com.example.totanpay.feature.purchase


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.PurchasePriceTextInputModifier
import com.example.totanpay.ui.component.PurchasePriceTextInput
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun PurchaseScreen(
    onPurchaseSelected: (String, Boolean) -> Unit,
    onBackClicked: () -> Unit
) {
    BackHandler {
        onBackClicked()
    }
    var amount: String by remember { mutableStateOf("") }
    var amountHasError: Boolean by remember { mutableStateOf(false) }
    var purchaseIdIsEnabled by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    ConstraintLayout(
        ConstraintSet {
            val purchasePriceTextInput = createRefFor("purchasePriceTextInput")
            val switchContainer = createRefFor("switchContainer")
            constrain(purchasePriceTextInput) {
                top.linkTo(parent.top,40.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
            }
            constrain(switchContainer) {
                top.linkTo(purchasePriceTextInput.bottom,10.dp)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
                width = Dimension.fillToConstraints
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(bottom = 0.dp)
    ) {


        PurchasePriceTextInput(
            modifier = Modifier
                .PurchasePriceTextInputModifier(isSmall = true)
                .layoutId("purchasePriceTextInput"),
            hasError = amountHasError,
            errorMessage = stringResource(R.string.enter_purchase_amount),
            title = stringResource(R.string.enter_purchase_amount),
            trailerTitle = stringResource(id = R.string.currency),
            value = amount,
            isSmall = true,
            onDone = {
                if (amount.isNotEmpty()) {
                    if (!purchaseIdIsEnabled) {
                        onPurchaseSelected(amount, false)
                    } else {
                        onPurchaseSelected(amount, true)
                    }
                } else {
                    showToast=true
                    amountHasError = true
                }
            }
        ) {
            showToast=false
            amount = it
            amountHasError = false
        }
        Row(
            modifier = Modifier
                .padding(start = 18.dp, end = 18.dp)
                .padding(top = 0.dp)
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
    }

}

@Composable
@Preview
fun PurchaseScreenPreview() {
    TotanPayTheme {
        PurchaseScreen(
            onPurchaseSelected = { amountValue, purchaseId ->
            }, onBackClicked = {})
    }
}