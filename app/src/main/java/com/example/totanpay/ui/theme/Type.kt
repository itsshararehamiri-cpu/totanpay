package com.example.totanpay.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.totanpay.R

val fontFamily = FontFamily(
    Font(R.font.danafanum_light)
)
val iranSansFanum = FontFamily(
    Font(R.font.peydafanum_regular)
)

val iranYekanRegular = FontFamily(
    Font(R.font.peydafanum_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(//okkkkkkkkkkkkkkkkkkkkkkkkkkkk
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 41.sp,
        //letterSpacing = 0.5.sp
    ),
    displayMedium = TextStyle(//okooooooooo
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 23.sp),
    displaySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(//okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.0.sp
    ),
    bodyMedium = TextStyle(//okkkkkkkkkk
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(//okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
        fontFamily = fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 29.sp,
      //  letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(//okkkkkkkkkkkkkkkkkk
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        //letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(//okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
        fontFamily = fontFamily,
        fontSize = 16.sp,
        lineHeight = 23.sp,
        fontWeight = FontWeight.Normal
        // letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    )
)
/*
@property displayLarge displayLarge is the largest display text.
 * @property displayMedium displayMedium is the second largest display text.
 * @property displaySmall displaySmall is the smallest display text.
 * @property headlineLarge headlineLarge is the largest headline, reserved for short, important text
 * or numerals. For headlines, you can choose an expressive font, such as a display, handwritten, or
 * script style. These unconventional font designs have details and intricacy that help attract the
 * eye.
 * @property headlineMedium headlineMedium is the second largest headline, reserved for short,
 * important text or numerals. For headlines, you can choose an expressive font, such as a display,
 * handwritten, or script style. These unconventional font designs have details and intricacy that
 * help attract the eye.
 * @property headlineSmall headlineSmall is the smallest headline, reserved for short, important
 * text or numerals. For headlines, you can choose an expressive font, such as a display,
 * handwritten, or script style. These unconventional font designs have details and intricacy that
 * help attract the eye.
 * @property titleLarge titleLarge is the largest title, and is typically reserved for
 * medium-emphasis text that is shorter in length. Serif or sans serif typefaces work well for
 * subtitles.
 * @property titleMedium titleMedium is the second largest title, and is typically reserved for
 * medium-emphasis text that is shorter in length. Serif or sans serif typefaces work well for
 * subtitles.
 * @property titleSmall titleSmall is the smallest title, and is typically reserved for
 * medium-emphasis text that is shorter in length. Serif or sans serif typefaces work well for
 * subtitles.
 * @property bodyLarge bodyLarge is the largest body, and is typically used for long-form writing as
 * it works well for small text sizes. For longer sections of text, a serif or sans serif typeface
 * is recommended.
 * @property bodyMedium bodyMedium is the second largest body, and is typically used for long-form
 * writing as it works well for small text sizes. For longer sections of text, a serif or sans serif
 * typeface is recommended.
 * @property bodySmall bodySmall is the smallest body, and is typically used for long-form writing
 * as it works well for small text sizes. For longer sections of text, a serif or sans serif
 * typeface is recommended.
 * @property labelLarge labelLarge text is a call to action used in different types of buttons (such
 * as text, outlined and contained buttons) and in tabs, dialogs, and cards. Button text is
 * typically sans serif, using all caps text.
 * @property labelMedium labelMedium is one of the smallest font sizes. It is used sparingly to
 * annotate imagery or to introduce a headline.
 * @property labelSmall labelSmall is one of the smallest font sizes. It is used sparingly to
 * annotate imagery or to introduce a headline.
 */