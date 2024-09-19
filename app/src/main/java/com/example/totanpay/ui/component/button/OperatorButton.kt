package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun OperatorButton(isSmall:Boolean,
    isSelected: Boolean = false, operator: Operator, modifier: Modifier, onClick: (Operator) -> Unit
) {
    Row(modifier = modifier .padding(horizontal = 4.dp)
        .clip(RoundedCornerShape(8.dp))
        .height(if(isSmall)50.dp else 40.dp)
        .border(
            1.dp,
            if (isSelected) operator.borderColor else MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(8.dp)
        )
        .clickable { onClick(operator) }
        .background(
            if (isSelected) operator.borderColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        )
        .clip(RoundedCornerShape(8.dp))) {
        Image(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(if(isSmall)40.dp else 32.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = operator.imaged),
            contentDescription = "back_button"
        )
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            text = operator.persianName,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
@Preview
fun OperatorButtonPreview() {
    TotanPayTheme {
        OperatorButton(isSmall = true,
            isSelected = true, Operator(
                code = 11,
                persianName = "ایرانسل",
                englishName = "irancel",
                voucherChargeMSG = "",
                borderColor = Color.Yellow,
                imaged = R.drawable.ic_irancel,
                chargeList = listOf()
            ), modifier = Modifier
        ) {

        }
    }
}
