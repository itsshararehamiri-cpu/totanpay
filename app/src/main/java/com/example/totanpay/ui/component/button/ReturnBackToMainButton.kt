package com.example.totanpay.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.common.isSmall

@Composable
fun ReturnBackToMainButton(isSmall:Boolean,
    modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp)
            .background(MaterialTheme.colorScheme.background)

    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(if(isSmall)R.string.return_back else R.string.return_back_to_main),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center
        )
    }
}