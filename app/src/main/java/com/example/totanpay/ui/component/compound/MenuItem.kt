package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.theme.DarkBlue2
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun MenuItem(
    modifier: Modifier, backgroundImageId: Int, iconId: Int,
    backgroundIconId: Int, title: String
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = backgroundImageId),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)

        )
        Column(
            Modifier
                .padding(top = 1.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .padding(start = 18.dp)
                .align(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(id = backgroundIconId),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .size(32.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter =
                    ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                )
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .size(32.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,

                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)//MaterialTheme.colorScheme.surface
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center

            )

        }
    }
}
