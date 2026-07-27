package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = AmberLight,
    onPrimary = Color.Black,
    primaryContainer = GoldAccent,
    onPrimaryContainer = Color.White,
    secondary = GoldAccent,
    onSecondary = Color.White,
    background = SlateDark,
    onBackground = SlateTextLight,
    surface = SlateCardDark,
    onSurface = SlateTextLight,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavySecondary,
    onPrimaryContainer = Color.White,
    secondary = GoldAccent,
    onSecondary = Color.White,
    tertiary = AmberLight,
    background = SlateLightBg,
    onBackground = SlateTextDark,
    surface = SlateLightCard,
    onSurface = SlateTextDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569)
  )

@Composable
fun ThreeBrothersTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

