package com.example.totanpay.ui.component.compound
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
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Red

@Composable
fun ExitItem(
    modifier: Modifier,
    backgroundImageId: Int,
    backgroundIconId: Int,
    title: String,
    isSmall:Boolean,
    onItemClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onItemClicked() }) {

        Image(
            painter = painterResource(id = backgroundImageId), contentDescription = "",
            modifier = modifier
                .fillMaxSize(), contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
        )

        Row(
            Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Box(modifier=Modifier.padding(start = 18.dp)){
                Image(
                    painter = painterResource(id = backgroundIconId),
                    contentDescription = "",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.FillBounds,
                )
            }
            Text(
                text = title,
                modifier = Modifier.padding(start = 10.dp),
                color = Red,
                style = MaterialTheme.typography.displayMedium

            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left_gray),
                contentDescription = "",
                modifier = modifier
                    .padding(end = 26.dp)
                    .size(24.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            )
        }
    }
}