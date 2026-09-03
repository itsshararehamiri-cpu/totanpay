package com.example.totanpay.ui.component.compound

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.R
import com.example.totanpay.ui.theme.FONT_SIZE_14

@Composable
fun I5000SettingsItem(
    modifier: Modifier = Modifier,
    iconImageId: Int?,
    title: String,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onItemClicked: () -> Unit
) {
    val isRtl = LocalLanguageState.current.isFarsiSelected.value
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable { onItemClicked() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconImageId != null) {
            Image(
                painter = painterResource(id = iconImageId),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = FONT_SIZE_14,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(
                id = if (isRtl) R.drawable.ic_arrow_left_gray else R.drawable.ic_arrow_right
            ),
            contentDescription = "",
            modifier = Modifier.size(14.dp)
        )
    }
}
