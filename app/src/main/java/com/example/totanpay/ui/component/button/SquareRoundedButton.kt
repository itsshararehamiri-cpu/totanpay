package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Black500
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun SquareRoundedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(Black500),
    ) {
        Image(
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_arrow_to_left),
            contentDescription = ""
        )
    }
}
@Composable
@Preview
fun SquareRoundedButtonPreview() {
    TotanPayTheme {
        SquareRoundedButton(onClick = {}, modifier = Modifier)
    }
}
