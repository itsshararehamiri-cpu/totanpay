package com.example.totanpay.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordDigitPlacement2(modifier: Modifier, value: String, showError: Boolean) {
    Box(
        modifier
            .size(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, if (!showError) MaterialTheme.colorScheme.onSurface else Red, RoundedCornerShape(8.dp))
    ) {

        Text(
            text = if (value.isEmpty()) "" else "*",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(
                textDirection = TextDirection.Ltr,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = modifier.align(Alignment.Center)
        )
    }
}