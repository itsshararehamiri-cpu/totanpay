package com.example.totanpay.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.ui.theme.Blue30
import com.example.totanpay.ui.theme.Blue40
import com.example.totanpay.ui.theme.DarkPurpule
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ApportionmentView(
    apportionment: Apportionment,
    onAddApportionment: (Apportionment) -> Unit,
    onEditClicked: (Apportionment) -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp)
            .padding(bottom = 5.dp, top = 5.dp)
    ) {
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Text(
                text = apportionment.bankName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            if (apportionment.amount.isNotEmpty()) {
                Row(Modifier.clickable {
                    onEditClicked(apportionment)
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_edit),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(if(isSmall(context = LocalContext.current))
                            Blue30 else DarkPurpule),
                    )
                    Text(
                        text = "${apportionment.amount} %",
                        color = if(isSmall(context = LocalContext.current))
                            Blue40 else DarkPurpule,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Box(
                    Modifier
                        .background(
                            color = Blue30,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable {
                            onAddApportionment(apportionment)
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_add),
                        modifier = Modifier.size(24.dp),
                        contentDescription = ""
                    )
                }
            }
        }
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                "شماره شبا",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                apportionment.IBAN,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun ApportionmentViewPreview() {
    TotanPayTheme {
        ApportionmentView(
            Apportionment(
                IBAN = "IR6037", amount = "20", bankName = "بانک شهر"
            ),
            onAddApportionment = {

            },
            onEditClicked = {

            }
        )
    }
}