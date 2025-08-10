package com.example.totanpay.ui.component.report

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.totanpay.ui.theme.Dimensions


@Composable
fun DateHeader(dateTransaction: String, isPaperReceipt: Boolean = false) {
    Text(
        text = dateTransaction,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = if (isPaperReceipt) Dimensions.FONT_SIZE_PAPER_RECEIPT else
                Dimensions.FONT_SIZE_RECEIPT,
            fontWeight = if (isPaperReceipt) FontWeight.Medium else FontWeight.Normal
        ),
        modifier = Modifier
            .padding(
                top = 3.dp,
                start = if (isPaperReceipt) 3.dp else 7.dp,
                end = if (isPaperReceipt) 3.dp else 7.dp
            )
            .fillMaxWidth()
            .padding(top = 3.dp),
        color = if (isPaperReceipt) Black else MaterialTheme.colorScheme.primary
    )
}

