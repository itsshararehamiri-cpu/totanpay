package com.example.totanpay.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.totanpay.common.isSmall

private val DarkColorScheme = darkColorScheme(
    primary = DeepBlue,
    secondary = BrightRed,
    tertiary = Color.White,
    primaryContainer = Color.White,
    surface = Black10,
    background =Black,
    onBackground = White200,
    onPrimary = Color.White,
    surfaceContainer = Black200,
    onSurface = White300,
    surfaceVariant = Color.White,
    outline = White200,
    onSecondary = Color.White,
    secondaryContainer = Navy,

)

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,//Red
    secondary = Black10,//DarkBlue
    tertiary = DarkBlue,
    primaryContainer = Color.Black,
    surface = White300,
    background = White400,
    onBackground = Black400,
    onPrimary = Color.White,
    surfaceContainer = White200,
    onSurface = Blackk,
    surfaceVariant = Gray100,
    outline = Black700,
    outlineVariant = Black500,
    onSecondary = Black10,
    secondaryContainer = White400
)
private val DarkColorI5000Scheme = darkColorScheme(
    primary = DeepBlue,
    secondary = BrightRed,
    tertiary = Color.White,
    primaryContainer = Color.White,
    surface = Black10,
    background =Black,
    onBackground = White200,
    onPrimary = Color.White,
    surfaceContainer = Black200,
    onSurface = White300,
    surfaceVariant = Color.White,
    outline = White200,
    onSecondary = Color.White,
    secondaryContainer = Navy,

    )

private val LightColorI5000Scheme = lightColorScheme(
    primary = DeepBlue,//Red
    secondary = Black10,//DarkBlue
    tertiary = DarkBlue,
    primaryContainer = Color.Black,
    surface = White,
    background = Color(0XFFF5F7FA),
    onBackground = Black400,
    onPrimary = Color.White,
    surfaceContainer = White200,
    onSurface = Black40,
    surfaceVariant = Gray100,
    outline = Black700,
    outlineVariant = Black500,
    onSecondary = Black10,
    secondaryContainer = White400
)


@Composable
fun TotanPayTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context= LocalContext.current
    val isSmall= isSmall(context)
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> if(isSmall) DarkColorI5000Scheme else DarkColorScheme
        else ->if(isSmall) LightColorI5000Scheme else LightColorScheme
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography =if(isSmall)TypographyI5000 else Typography,
            content = content
        )
    }
}
/*
private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,//Red
    secondary = BrightRed,//DarkBlue
    //tertiary = Pink80,
    primaryContainer = Color.White,
    surface = Color.White,
    background = White400,
    onBackground = Black400,
    onPrimary = Color.White,
    surfaceContainer = White200,
    onSurface = Blackk,
    surfaceVariant = Gray100,
    outline = Black700,
    outlineVariant = Black500, onSecondary = NavyBlue,
    secondaryContainer = White400
)
private val DarkColorScheme = darkColorScheme(
    primary = DarkRed,
    secondary = DarkBlue,
    tertiary = DarkBlue,
    primaryContainer = Color.Black,
    surface = Black100,
    background = Black200,
    onBackground = White200,
    onPrimary = Color.White,
    surfaceContainer = Black200,
    onSurface = White300,
    surfaceVariant = Black200,
    outline = White200,
    onSecondary = Color.White,
    secondaryContainer = Navy
)
 */