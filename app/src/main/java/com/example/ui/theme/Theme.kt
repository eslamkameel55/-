package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = StudyBluePrimary,
    secondary = StudySky,
    tertiary = StudyOrangeAccent,
    background = SlateDarkBg,
    surface = SlateCardBg,
    onPrimary = Color.White,
    onSecondary = SlateDarkBg,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    outlineVariant = SlateBorder
  )

private val LightColorScheme =
  lightColorScheme(
    primary = StudyBlueDark,
    secondary = StudySky,
    tertiary = StudyOrangeAccent,
    background = LightBg,
    surface = LightCard,
    onPrimary = Color.White,
    onSecondary = TextPrimaryLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    outlineVariant = LightBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabling Dynamic color to strictly preserve the Study Smart brand colors (Blue/Orange)
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
