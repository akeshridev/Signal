package com.ashish.signal.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Subtle scale-down feedback on press, layered on top of whatever
 * [interactionSource] already drives the component's default ripple —
 * pass the same InteractionSource you give the clickable/Button so both
 * effects react to the same press state.
 */
@Composable
fun Modifier.pressScale(interactionSource: InteractionSource): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "press-scale"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
