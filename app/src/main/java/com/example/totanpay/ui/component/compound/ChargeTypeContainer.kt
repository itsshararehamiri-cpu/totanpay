package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.theme.DarkBlue20
import com.example.totanpay.ui.theme.DarkPurpule
import com.example.totanpay.ui.theme.DeepBlue
import com.example.totanpay.ui.theme.Purpule
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.TurquoiseBlue

@Composable
fun ChargeTypeContainer(
    modifier: Modifier, isTopUpSelected: Boolean = true, onVoucherSelected: () -> Unit,
    onTopUpSelected: () -> Unit
) {
    val selectedModifier = Modifier.height(55.dp)
        .padding(top = 5.dp, bottom = 5.dp, start = 0.dp)
        .background(
            color = DarkBlue20,
            shape = RoundedCornerShape(12.dp)
        )
    Row(
        modifier = modifier
            .fillMaxWidth().height(60.dp)
            .background(
                color = TurquoiseBlue.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = if (isTopUpSelected) {
                Modifier.height(55.dp)
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            } else {
                selectedModifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            }.clickable {
                onVoucherSelected()
            }

        ) {
            Text(
             TransactionType.VOUCHER.title,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center, color =  if (!isTopUpSelected) {
                    Color.White
                }
                else Color.Black, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
            )
        }
        Box(
            modifier = if (!isTopUpSelected) {
                Modifier.height(55.dp)
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            } else {
                selectedModifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            }.clickable {
                onTopUpSelected()
            }
        ) {
            Text(
                TransactionType.TOPUP.title,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center, color =  if (isTopUpSelected) {
                    Color.White
                }
                else Color.Black, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
            )
        }
    }
}

@Composable
@Preview
fun ChargeTypeContainerPreview() {
    TotanPayTheme   {
        ChargeTypeContainer(modifier = Modifier, isTopUpSelected = true, onVoucherSelected = {}, onTopUpSelected = {})
    }
}