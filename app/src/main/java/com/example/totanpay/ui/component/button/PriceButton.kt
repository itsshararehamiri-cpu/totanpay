package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun PriceButton(
    selected: Boolean = false,
    title: String,
    modifier: Modifier,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .wrapContentWidth()
            .height(40.dp)
            .border(
                1.dp,
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelected(title) }
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        if(!selected){
            Image(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(
                    id =
                        R.drawable.ic_unselected_amount
                ),
                contentDescription = title,
                colorFilter =  ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
        else{
            Image(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(28.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(
                    id =  R.drawable.ic_selected_price
                ),
                contentDescription = title,
                colorFilter =  ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            text = "${title.formatAmount()}ریال",
            color = MaterialTheme.colorScheme.onBackground,style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
fun PriceButtonPreview() {
    TotanPayTheme {
        PriceButton(
            title = "ایرانسل", modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {}
    }
}
