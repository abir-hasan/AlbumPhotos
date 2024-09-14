package com.example.albumphotos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = lightColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    inversePrimary = InversePrimary,
    secondary = Secondary80,
    onSecondary = Secondary20,
    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    background = Neutral6,
    onBackground = Neutral90,
    surface = Neutral24,
    onSurface = Neutral90,
    error = Error80,
    onError = Error20,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Primary100,
    inversePrimary = InversePrimary,
    secondary = Secondary40,
    onSecondary = Secondary100,
    tertiary = Tertiary40,
    onTertiary = Tertiary100,
    background = Neutral98,
    onBackground = Neutral10,
    surface = Neutral87,
    onSurface = Neutral10,
    error = Error40,
    onError = Error100,
)

@Composable
fun AlbumPhotosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
