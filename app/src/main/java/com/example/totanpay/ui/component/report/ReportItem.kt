package com.example.totanpay.ui.component.report

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ReportItem(modifier: Modifier, backgroundImageId:Int, title: String, onItemClicked: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth().height(80.dp)
            .clickable { onItemClicked() }
    ) {
        Image(
            painter = painterResource(id =backgroundImageId),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth().height(100.dp),
            contentScale = ContentScale.FillBounds,colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
        )
        Row(
            Modifier.padding(top = 20.dp).padding(horizontal = 20.dp)
                .fillMaxSize().align(Alignment.Center)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(end = 20.dp),
                color =   MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium

            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left_gray),
                contentDescription = title,
                modifier = modifier
                    .padding(end = 26.dp)
                    .size(24.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            )

        }
    }
}
@Composable
@Preview
fun ReportItemPreview() {
    TotanPayTheme {
        ReportItem(
            modifier = Modifier
            ,
            backgroundImageId = R.drawable.background_last_transaction,
            title = stringResource(id = R.string.last_transaction)
        ) {
        }
    }
}