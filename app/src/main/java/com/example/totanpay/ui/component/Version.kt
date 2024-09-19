package com.example.totanpay.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.totanpay.R

@Composable
fun Version(modifier: Modifier) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    Text(
        text = stringResource(R.string.version_label, packageInfo.versionName ?: ""),
        modifier = modifier,
        color = Color.White,
        style = MaterialTheme.typography.bodySmall ,
        textAlign = TextAlign.Center
    )
}