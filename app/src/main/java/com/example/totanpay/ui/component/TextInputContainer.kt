package com.example.totanpay.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.ui.theme.Dimensions.MARGIN_TOP_TEXTFIELD_WITH_TITLE

@Composable
fun TextInputContainer(
    modifier: Modifier,
    title: String,
    hasError: Boolean = false,
    errorMessage: String,
    isSmall: Boolean,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .padding(start = if (isSmall) 10.dp else 5.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium ,
            textAlign = TextAlign.Start
        )
        Box(
            modifier = Modifier
                .padding(top = if (isSmall) MARGIN_TOP_TEXTFIELD_WITH_TITLE else MARGIN_TOP_TEXTFIELD_WITH_TITLE)
                .fillMaxWidth()
                .border(
                    width = 1.dp, brush = Brush.horizontalGradient(
                        colors = if (!hasError) listOf(
                            MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outline
                        )
                        else listOf(
                            Red.copy(alpha = 0.8f), Red.copy(alpha = 1f)
                        )
                    ), shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)

        ) {
            content()
        }
        if (hasError && errorMessage.isNotEmpty()) Text(
            modifier = Modifier
                .padding(
                    start = if (isSmall) 8.dp else 5.dp,
                    top = if (isSmall) 7.dp else 5.dp
                )
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = Red,
            textAlign = TextAlign.Start
        )
    }
}