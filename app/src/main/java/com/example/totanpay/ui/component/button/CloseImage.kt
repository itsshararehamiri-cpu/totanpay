package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun CloseImage(
    modifier: Modifier, onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(32.dp)
            .background(color = Color.White, shape = CircleShape)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_black_close),
            contentDescription = "Circular image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp * 0.6f) // Adjust image size relative to the circle
                .clip(CircleShape)
                .padding(5.dp) // Make the image itself circular
        )
    }
}

@Composable
@Preview
fun CloseImagePreview() {
    TotanPayTheme {
        CloseImage(Modifier) {}
    }
}