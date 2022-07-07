package com.mlr_apps.rickandmorty.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

private val DarkColorPalette = darkColors(
    primary = Color.Yellow,
    background = Color(0xFF101010),
    onBackground = Color.White,
    surface = Color(0xFF303030),
    onSurface = Color.White

)

private val LightColorPalette = lightColors(
    primary = Color(0xF000BCD4),
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFF8F8F8),
    onSurface = Color.Black,

)

@Composable
fun RickAndMortyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}