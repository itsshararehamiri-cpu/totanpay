package com.example.totanpay.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrintButton(
    modifier: Modifier = Modifier, enabled: Boolean = true, title: String, onClick: () -> Unit = {}
) {
    Button( modifier = modifier
        .clip(RoundedCornerShape(4.dp))
        .padding(horizontal = 3.dp),
         enabled = enabled, onClick = {
             onClick()
        }
    ) {
        Text(
            modifier = Modifier,
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
        )
    }

//    Box(
//        modifier = modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(if (enabled) MaterialTheme.colorScheme.primary else Gray.copy(alpha = 0.5f))
//            .clip(RoundedCornerShape(8.dp))
//            .clickable {
//                if (enabled)
//                    onClick()
//            }
//            .padding(horizontal = 10.dp)
//    ) {
//        Text(
//            modifier = Modifier.align(Alignment.Center),
//            text = title,
//            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = if (enabled) 1f else 0.45f),
//            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
//        )
//    }
}


