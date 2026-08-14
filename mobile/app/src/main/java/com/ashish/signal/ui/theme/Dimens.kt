package com.ashish.signal.ui.theme

import androidx.compose.ui.unit.dp

/** Single source of truth for spacing/sizing so no composable hardcodes a raw dp literal. */
object Dimens {
    val spaceXs = 4.dp
    val spaceSm = 8.dp
    val spaceMd = 16.dp
    val spaceLg = 24.dp

    val cardCornerRadius = 20.dp
    val cardElevation = 6.dp
    /** Half-gap applied above and below each card so adjacent cards sit 12dp apart. */
    val cardSpacing = 6.dp
    /** Screen-edge inset for card content (design spec: px-5 / 20dp). */
    val screenPadding = 20.dp

    val avatarSize = 40.dp
    val illustrationSize = 48.dp
    val askAiButtonHeight = 32.dp

    /** Android accessibility guidance minimum for any tappable control. */
    val minTouchTarget = 48.dp
}
