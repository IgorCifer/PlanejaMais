package com.example.planeja.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PlanejaAccent2,
    onPrimary = Color.White,

    secondary = PlanejaAccent3,
    onSecondary = Color.White,

    tertiary = PlanejaAccent1,
    onTertiary = Color.Black,

    background = Color(0xFF050712),
    onBackground = Color(0xFFF5F5F5),

    surface = Color(0xFFA8A8A8),
    onSurface = Color(0xFFE5E5E5),

    surfaceVariant = Color(0xFF181C28),
    onSurfaceVariant = Color(0xFFCACACA),

    outline = Color(0x33FFFFFF)
)



private val LightColorScheme = lightColorScheme(
    primary = PlanejaAccent2,
    onPrimary = Color.White,
    secondary = PlanejaAccent1,
    onSecondary = PlanejaTextDark,
    tertiary = PlanejaAccent3,
    background = PlanejaBgLight,
    onBackground = PlanejaTextDark,
    surface = Color.White,
    onSurface = PlanejaTextDark,
    surfaceVariant = PlanejaSecondary,
    onSurfaceVariant = PlanejaTextGray,
    outline = Color(0x1A000000),
)



@Composable
fun PlanejaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            val useDarkIcons = !darkTheme

            insetsController.isAppearanceLightStatusBars = useDarkIcons
            insetsController.isAppearanceLightNavigationBars = useDarkIcons
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


