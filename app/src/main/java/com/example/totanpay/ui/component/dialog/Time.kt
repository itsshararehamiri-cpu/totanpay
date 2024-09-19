package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.totanpay.ui.component.HorizontalDivider

@Composable
fun CenteredLazyColumn(
    items: List<String>,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val listState = rememberLazyListState()

    // Calculate the selected index dynamically
    val selectedItemIndexHour by remember {
        derivedStateOf {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                visibleItems[visibleItems.size / 2].index // Middle item of the visible range
            } else {
                0
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(150.dp), // Restrict height to show only 3 items (adjust as needed)
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 16.dp) // Ensure items have spacing
    ) {
        itemsIndexed(items) { index, item ->
            val isSelected = index == selectedItemIndexHour
            if(isSelected)
            {
                HorizontalDivider(modifier=Modifier.fillMaxWidth(1f),  isPaperReceipt =false )
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.Transparent)
                        .padding(16.dp),
                    style = if (isSelected) {
                        MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                    } else {
                        MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    },
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(modifier=Modifier.fillMaxWidth(1f), isPaperReceipt = false)
            }
            else{
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.Transparent)
                        .padding(16.dp),
                    style = if (isSelected) {
                        MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                    } else {
                        MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
