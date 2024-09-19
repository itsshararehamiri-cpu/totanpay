package com.example.totanpay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.totanpay.ui.theme.Dimensions.BUTTON_HEIGHT
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAINBUTTON
import com.example.totanpay.ui.theme.START_PADDING

val TextInputModifier = Modifier
    .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
    .fillMaxWidth()
    .wrapContentHeight()
//  .height(HEIGHT_TEXT_INPUT)

val ListModifier = Modifier
    .padding(horizontal = END_PADDING)
    .padding(top = 10.dp)
    .fillMaxWidth()




val CancelButtonModifier: Modifier =

    Modifier
        .padding(horizontal = 10.dp)
        .padding(bottom = MARGIN_BOTTOM_MAINBUTTON)
        .height(BUTTON_HEIGHT)
        .fillMaxWidth()

val ReturnBackToMainButtonModifier: Modifier =

    Modifier
        .padding(horizontal = 10.dp)
        .padding(bottom = MARGIN_BOTTOM_MAINBUTTON)
        .height(BUTTON_HEIGHT)
        .fillMaxWidth()

val SmallReturnBackToMainButtonModifier: Modifier =

    Modifier
        .padding(horizontal = 5.dp)
        .padding(bottom = 6.dp)
        .height(BUTTON_HEIGHT)
        .fillMaxWidth()

val PrintButtonModifier: Modifier =

    Modifier
        .padding(horizontal = 10.dp)
        .padding(bottom = MARGIN_BOTTOM_MAINBUTTON)
        .height(BUTTON_HEIGHT)
        .fillMaxWidth()

val SmallPrintButtonModifier: Modifier =

    Modifier
        .padding(horizontal = 5.dp)
        .padding(bottom = 6.dp)
        .height(BUTTON_HEIGHT)
        .fillMaxWidth()

@Composable
fun DialogContainerModifier(): Modifier {
    return Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp))
        .fillMaxWidth()
}

