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

private val DarkColorScheme =
  darkColorScheme(
    primary = StudyBluePrimary,
    secondary = StudyBlueDark,
    tertiary = StudyOrangeAccent,
    background = SlateDarkBg,
    surface = SlateCardBg,
    onPrimary = LightCard,
    onSecondary = LightCard,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = StudyBlueDark,
    secondary = StudyBluePrimary,
    tertiary = StudyOrangeAccent,
    background = LightBg,
    surface = LightCard,
    onPrimary = LightCard,
    onSecondary = LightCard,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
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
