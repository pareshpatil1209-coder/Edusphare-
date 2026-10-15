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

private val DarkColorScheme = darkColorScheme(
    primary = EduIndigoPrimaryDark,
    onPrimary = Color(0xFF1E1B4B),
    secondary = EduSkySecondaryDark,
    onSecondary = Color(0xFF082F49),
    tertiary = EduAmberTertiaryDark,
    onTertiary = Color(0xFF451A03),
    background = EduBackgroundDark,
    onBackground = Color(0xFFF8FAFC),
    surface = EduSurfaceDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = EduSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = EduIndigoPrimary,
    onPrimary = Color.White,
    secondary = EduSkySecondary,
    onSecondary = Color.White,
    tertiary = EduAmberTertiary,
    onTertiary = Color.White,
    background = EduBackgroundLight,
    onBackground = Color(0xFF0F172A),
    surface = EduSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = EduSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun EduSphereTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
