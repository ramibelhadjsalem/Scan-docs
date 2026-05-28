package com.scandoc.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val scanDocColorScheme = darkColorScheme(
    primary = ScanDocColors.Signal,
    onPrimary = ScanDocColors.Ink,
    primaryContainer = ScanDocColors.SignalGlow,
    onPrimaryContainer = ScanDocColors.Signal,
    secondary = ScanDocColors.Warm,
    onSecondary = ScanDocColors.Ink,
    tertiary = ScanDocColors.Deep,
    onTertiary = ScanDocColors.Text,
    background = ScanDocColors.Ink,
    onBackground = ScanDocColors.Text,
    surface = ScanDocColors.Ink2,
    onSurface = ScanDocColors.Text,
    surfaceVariant = ScanDocColors.Ink3,
    onSurfaceVariant = ScanDocColors.Text2,
    outline = ScanDocColors.Line,
    error = ScanDocColors.Danger,
    onError = ScanDocColors.Text,
)

@Composable
fun ScanDocTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = scanDocColorScheme,
        typography = scanDocTypography(),
        shapes = scanDocShapes(),
        content = content,
    )
}
