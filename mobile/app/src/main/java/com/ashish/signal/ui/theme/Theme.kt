package com.ashish.signal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = md_light_primary,
    onPrimary = md_light_onPrimary,
    primaryContainer = md_light_primaryContainer,
    onPrimaryContainer = md_light_onPrimaryContainer,
    secondary = md_light_secondary,
    onSecondary = md_light_onSecondary,
    secondaryContainer = md_light_secondaryContainer,
    onSecondaryContainer = md_light_onSecondaryContainer,
    tertiary = md_light_tertiary,
    onTertiary = md_light_onTertiary,
    tertiaryContainer = md_light_tertiaryContainer,
    onTertiaryContainer = md_light_onTertiaryContainer,
    background = md_light_background,
    onBackground = md_light_onBackground,
    surface = md_light_surface,
    onSurface = md_light_onSurface,
    surfaceVariant = md_light_surfaceVariant,
    onSurfaceVariant = md_light_onSurfaceVariant,
    outline = md_light_outline,
    error = md_light_error,
    onError = md_light_onError,
    errorContainer = md_light_errorContainer,
    onErrorContainer = md_light_onErrorContainer,
)

private val DarkColorScheme = darkColorScheme(
    primary = md_dark_primary,
    onPrimary = md_dark_onPrimary,
    primaryContainer = md_dark_primaryContainer,
    onPrimaryContainer = md_dark_onPrimaryContainer,
    secondary = md_dark_secondary,
    onSecondary = md_dark_onSecondary,
    secondaryContainer = md_dark_secondaryContainer,
    onSecondaryContainer = md_dark_onSecondaryContainer,
    tertiary = md_dark_tertiary,
    onTertiary = md_dark_onTertiary,
    tertiaryContainer = md_dark_tertiaryContainer,
    onTertiaryContainer = md_dark_onTertiaryContainer,
    background = md_dark_background,
    onBackground = md_dark_onBackground,
    surface = md_dark_surface,
    onSurface = md_dark_onSurface,
    surfaceVariant = md_dark_surfaceVariant,
    onSurfaceVariant = md_dark_onSurfaceVariant,
    outline = md_dark_outline,
    error = md_dark_error,
    onError = md_dark_onError,
    errorContainer = md_dark_errorContainer,
    onErrorContainer = md_dark_onErrorContainer,
)

/**
 * App theme with a deliberate, fully-specified light/dark palette (see
 * Color.kt). Dynamic color is intentionally not used — Signal keeps a
 * consistent brand identity across devices rather than tinting to wallpaper.
 * Defaults to dark regardless of system setting: a crypto price tracker is
 * used far more often as a dark-first surface than a light one.
 */
@Composable
fun SignalTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
