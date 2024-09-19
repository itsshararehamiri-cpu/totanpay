package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.LightBlue
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun I5000SettingsItem(
    modifier: Modifier,
    iconImageId: Int?,
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onItemClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .clickable { onItemClicked() }, contentAlignment = Alignment.Center
    ) {


        Row(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconImageId != null) Image(
                painter = painterResource(id = iconImageId),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(28.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = title,
                modifier = Modifier.padding(start = 10.dp),
                color = textColor,
                style = MaterialTheme.typography.displayMedium

            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left_gray),
                contentDescription = "",
                modifier = modifier
                    .padding(end = 26.dp)
                    .size(24.dp),
                colorFilter = ColorFilter.tint(LightBlue)
            )
        }
    }
}

@Composable
@Preview
fun I5000SettingsItemPreview() {
    TotanPayTheme {
        I5000SettingsItem(
            modifier = Modifier.layoutId("connectionSettings"),
            iconImageId = R.drawable.ic_connection_settings,
            title = stringResource(id = R.string.connection_settings)
        ) {}
    }
}