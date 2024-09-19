package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.vvv
import com.example.totanpay.ui.theme.y
import com.example.totanpay.ui.theme.yyyy

@Composable
fun SmallMenuItem(
    modifier: Modifier, backgroundImageId: Int, iconId: Int,
    backgroundIconId: Int, title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth().background(
                yyyy, shape = RoundedCornerShape(8.dp)
                ).border(width = 0.1.dp, color = vvv.copy(alpha = 0.32f),shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            Modifier
                .padding(top = 1.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(38.dp),
                contentScale = ContentScale.FillBounds, alignment = Alignment.Center
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally).padding(horizontal = 3.dp),
                color = y,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight= FontWeight.Bold),
                textAlign = TextAlign.Center

            )

        }
    }
}


@Composable
@Preview
fun SmallMenuItemPreview(){
    TotanPayTheme {
        SmallMenuItem(
            modifier = Modifier
                .layoutId("voucherBox"),
            backgroundImageId = R.drawable.main_item_background_to_right,
            iconId = R.drawable.ic_i5000_purchase,
            backgroundIconId = R.drawable.background_voucher,
            title = TransactionType.VOUCHER.title
        )
    }
}
/*
 Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth().background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            Modifier
                .padding(top = 1.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)
                .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = backgroundIconId),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(32.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary), alignment = Alignment.Center
                )
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(32.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface), alignment = Alignment.Center
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally).padding(horizontal = 3.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center

            )

        }
    }
 */