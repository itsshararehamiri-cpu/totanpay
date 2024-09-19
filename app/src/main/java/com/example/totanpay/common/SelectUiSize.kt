package com.example.totanpay.common

import android.os.Build
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.totanpay.MainActivity

@Composable
fun SelectUiSize(smallContent: @Composable () -> Unit,
                 largeContent: @Composable () -> Unit) {
    val width: Int
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = (context as MainActivity).windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        width = bounds.width()
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
    }
    if (width < 500) {
        smallContent()
    } else {
       largeContent()

    }
}