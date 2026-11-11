package com.example.detectordefibrosisp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = SurfaceWhite,
    primaryContainer = SecondaryBlue,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    onBackground = TextPrimary
)

@Composable
fun DetectorDeFibrosisPTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
