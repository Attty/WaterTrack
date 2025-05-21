package com.example.watertrack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.watertrack.R


private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

/**
 * Define the Montserrat font family
 */
val MontserratFont = GoogleFont("Montserrat")

val MontserratFontFamily = FontFamily(
    Font(googleFont = MontserratFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = MontserratFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = MontserratFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = MontserratFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = MontserratFont, fontProvider = provider, weight = FontWeight.Bold)
)

/**
 * Define the Jost font family as an alternative
 */
val JostFont = GoogleFont("Jost")

val JostFontFamily = FontFamily(
    Font(googleFont = JostFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = JostFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = JostFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = JostFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = JostFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Choose which font family to use throughout the app
val AppFontFamily = MontserratFontFamily // Change to JostFontFamily if preferred

/**
 * Define app typography with custom font
 */
val WaterTrackTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    displayMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)