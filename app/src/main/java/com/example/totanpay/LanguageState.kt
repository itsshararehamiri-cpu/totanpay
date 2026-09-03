package com.example.totanpay

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf


class LanguageState {
    val isFarsiSelected = mutableStateOf(true)
}

val LocalLanguageState = staticCompositionLocalOf<LanguageState> {
    error("No GlobalState provided")
}