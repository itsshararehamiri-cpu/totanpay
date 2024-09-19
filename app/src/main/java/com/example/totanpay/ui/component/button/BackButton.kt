package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.END_PADDING

@Composable
fun BackButton(title: String, modifier: Modifier, onBackButtonClicked: () -> Unit) {
    Row(
        modifier = modifier
            .clickable { onBackButtonClicked() }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = END_PADDING, top = 20.dp)
                .size(28.dp),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "back_button",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
        Text(
            text = title, color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 3.dp, top = 20.dp),
        )
    }
}
@Composable
@Preview
fun BackButtonPreview() {

}
