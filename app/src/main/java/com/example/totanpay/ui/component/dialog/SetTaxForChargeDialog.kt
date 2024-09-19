package com.example.totanpay.ui.component.dialog


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.ui.component.CancelButton
import com.example.totanpay.ui.component.Keypad
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_TEXTFIELD_WITH_TITLE
import com.example.totanpay.ui.theme.MARGIN_SIDE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTaxForChargeDialog(
    modifier: Modifier,
    taxForCharge: String = "",
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: (String) -> Unit
) {
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    var tax: String by remember {
        mutableStateOf("")
    }
    var showError by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        keyboard?.hide()
    }
    LaunchedEffect(Unit) {
        tax = taxForCharge
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.plz_enter_percent_of_tax_irancell_charge),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

                )
                Row(
                    Modifier
                        .padding(top = MARGIN_TOP_TEXTFIELD_WITH_TITLE)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ) {
                    Box(
                        modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterVertically)
                            .border(
                                1.dp, if (!showError) MaterialTheme.colorScheme.outline else Red,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = tax.toEnglishNumber(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDirection = TextDirection.Ltr,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 5.dp, bottom = 5.dp)
                        )
                    }

                }


                Keypad(
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth(), onKeyClicked = {
                        if ("$tax$it".toInt() in 0..100)
                            tax = "$tax$it".toEnglishNumber()
                        else {
                            errorMessage = context.getString(R.string.percent_must_be_in_range)
                        }
                    }, onClearKeyClicked = {
                        tax = tax.dropLast(1)
                    }, onTikKeyClicked = {
                        showError = false
                        if (tax.isNotEmpty()) {
                            if (tax.toInt() <= 100)
                                onConfirmButtonClicked(tax.toEnglishNumber())
                            else {
                                errorMessage = context.getString(R.string.percent_must_be_in_range)
                            }
                        } else {
                            showError = true
                        }
                    })
                CancelButton(
                    modifier = Modifier
                        .padding(horizontal = 0.dp)
                        .padding(top = 32.dp, bottom = 14.dp)
                        .fillMaxWidth()
                        .height(BUTTON_HEIGHT)
                        .padding(horizontal = MARGIN_SIDE)
                ) {
                    onCancelButtonClicked()
                }
            }
            if (errorMessage.isNotEmpty()) {
                ShowToast(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .align(Alignment.Center),
                    message = errorMessage
                ) {
                    errorMessage = ""
                }
            }
        }
    }
}