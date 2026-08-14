package com.ashish.signal.ui.theme

import androidx.compose.ui.graphics.Color

// Indigo-blue brand primary with a warm rose tertiary accent. Full tonal
// roles for both light and dark are defined explicitly in Theme.kt rather
// than relying on Material's stock purple defaults or dynamic color.

// Light scheme
val md_light_primary = Color(0xFF3457D5)
val md_light_onPrimary = Color(0xFFFFFFFF)
val md_light_primaryContainer = Color(0xFFDCE1FF)
val md_light_onPrimaryContainer = Color(0xFF00174B)
val md_light_secondary = Color(0xFF5B5D72)
val md_light_onSecondary = Color(0xFFFFFFFF)
val md_light_secondaryContainer = Color(0xFFDFE1F9)
val md_light_onSecondaryContainer = Color(0xFF181A2C)
val md_light_tertiary = Color(0xFF77536D)
val md_light_onTertiary = Color(0xFFFFFFFF)
val md_light_tertiaryContainer = Color(0xFFFFD8EC)
val md_light_onTertiaryContainer = Color(0xFF2D1228)
val md_light_background = Color(0xFFFBF8FF)
val md_light_onBackground = Color(0xFF1B1B1F)
val md_light_surface = Color(0xFFFBF8FF)
val md_light_onSurface = Color(0xFF1B1B1F)
val md_light_surfaceVariant = Color(0xFFE3E1EC)
val md_light_onSurfaceVariant = Color(0xFF46464F)
val md_light_outline = Color(0xFF767680)
val md_light_error = Color(0xFFBA1A1A)
val md_light_onError = Color(0xFFFFFFFF)
val md_light_errorContainer = Color(0xFFFFDAD6)
val md_light_onErrorContainer = Color(0xFF410002)

// Dark scheme — deep navy-black crypto-app palette (distinct surface tone so
// cards lift off the background instead of the flat same-tone look above).
val md_dark_primary = Color(0xFF7C9CFF)
val md_dark_onPrimary = Color(0xFF041338)
val md_dark_primaryContainer = Color(0xFF223B82)
val md_dark_onPrimaryContainer = Color(0xFFDCE3FF)
val md_dark_secondary = Color(0xFFB6BEDA)
val md_dark_onSecondary = Color(0xFF262B3F)
val md_dark_secondaryContainer = Color(0xFF3A4058)
val md_dark_onSecondaryContainer = Color(0xFFDBE1FF)
val md_dark_tertiary = Color(0xFFF3C34D)
val md_dark_onTertiary = Color(0xFF3E2E00)
val md_dark_tertiaryContainer = Color(0xFF594400)
val md_dark_onTertiaryContainer = Color(0xFFFFE289)
val md_dark_background = Color(0xFF000000)
val md_dark_onBackground = Color(0xFFCBCDD3)
val md_dark_surface = Color(0xFF18191E)
val md_dark_onSurface = Color(0xFFCBCDD3)
val md_dark_surfaceVariant = Color(0xFF212227)
val md_dark_onSurfaceVariant = Color(0xFF9C9FA8)
val md_dark_outline = Color(0xFF3A3B42)
val md_dark_error = Color(0xFFFFB4AB)
val md_dark_onError = Color(0xFF690005)
val md_dark_errorContainer = Color(0xFF93000A)
val md_dark_onErrorContainer = Color(0xFFFFDAD6)

// Semantic price-movement colors, not part of the Material role system —
// used directly by token cards to color 24h change regardless of theme.
val crypto_gain = Color(0xFF00D18F)
val crypto_loss = Color(0xFFFF5A5F)

// Token card surface: flat fill + hairline border, no tonal elevation glow —
// matches the reference design's deliberately low-glare dark card.
val card_background = Color(0xFF1C1C20)
val card_border = Color(0xFF2E2E32)
