package com.example.totanpay.common

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import com.example.totanpay.MainActivity

fun isSmall(context:Context):Boolean{
    val width: Int
    val height: Int
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = (context as MainActivity).windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        width = bounds.width()
        height = bounds.height()
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
    }
    if (width < 500) {
        return true
    } else {
        return false

    }
}