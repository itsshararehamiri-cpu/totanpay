package com.example.totanpay.feature.purchase


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.example.totanpay.LocalDeviceManager
import com.example.totanpay.R
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.ui.ReturnBackToMainButtonModifier
import com.example.totanpay.ui.component.Keypad
import com.example.totanpay.ui.component.button.ReturnBackToMainButton
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_TEXTFIELD_WITH_TITLE
import kotlinx.coroutines.launch

@Composable
fun PurchaseIdBottomDialog(
    modifier: Modifier,
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: (String) -> Unit
) {

    val context = LocalContext.current
    val device = LocalDeviceManager.current
    var purchaseId: String by remember {
        mutableStateOf("")
    }
    var showError by remember {
        mutableStateOf(false)
    }
    val coroutineScope= rememberCoroutineScope()
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 18.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    coroutineScope.launch {
                        device.scan(
                            context = context,
                            onSuccess =
                            {
                                purchaseId = it
                            },
                            onTimeout = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onTimeout")
                            },
                            onError = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onError")
                            },
                            onCancel = {
                                Log.d("TAG", "PurchaseIdBottomDialog: onCancel")
                            })
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.scan__2_),
                contentDescription = "",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )
            Image(
                painter = painterResource(id = R.drawable.scan__1_),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }

        Text(
            text = stringResource(R.string.please_enter_purchase_id),
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium

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
                    text = purchaseId.toEnglishNumber(),
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
                purchaseId = "$purchaseId$it".toEnglishNumber()
            }, onClearKeyClicked = {
                purchaseId = purchaseId.dropLast(1)
            }, onTikKeyClicked = {
                showError = false
                if (purchaseId.isNotEmpty())
                    onConfirmButtonClicked(purchaseId.toEnglishNumber())
                else showError = true
            })
        ReturnBackToMainButton(isSmall = false,
            modifier = ReturnBackToMainButtonModifier

        ) {
            onCancelButtonClicked()
        }

}}