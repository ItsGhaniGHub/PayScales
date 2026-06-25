package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldGreenDark,
    secondary = MintSecondary,
    tertiary = GoldAccentDark,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = Color(0xFF0A1711),
    onSecondary = Color(0xFF0A1711),
    onTertiary = Color(0xFF0A1711),
    onBackground = Color.White,
    onSurface = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldGreenLight,
    secondary = MintLight,
    tertiary = GoldAccentLight,
    background = LightBg,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = OnBgLight,
    onSurface = OnBgLight,
    secondaryContainer = SecondaryBgLight,
    outline = OutlineLight,
    outlineVariant = Color(0xFFE2E9E0)
  )

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Forced to false for elegant Light Mode
  dynamicColor: Boolean = false, // Disabled dynamic colors to keep our premium emerald-and-mint branding
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
