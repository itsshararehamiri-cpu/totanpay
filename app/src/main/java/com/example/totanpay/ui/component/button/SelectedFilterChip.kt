package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.totanpay.R

@Composable
fun SelectedFilterChip(
    modifier: Modifier = Modifier,
    label: String,
    onRemove: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_close_circle),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .size(18.dp)
                .clickable { onRemove() }
        )
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
