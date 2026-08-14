package com.ashish.signal.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ashish.signal.ui.theme.Dimens

/**
 * Placeholder shown in place of a TokenCardComponent while the home screen
 * is loading. Mirrors the real card's layout (avatar, name/price block,
 * insight line, button) so the shimmer-to-content swap doesn't jump around.
 */
@Composable
fun TokenCardSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.cardSpacing, horizontal = Dimens.screenPadding)
            // A single description for the whole placeholder rather than one per
            // shimmering bar — TalkBack shouldn't announce four unlabeled shapes.
            .semantics { contentDescription = "Loading token" },
        shape = RoundedCornerShape(Dimens.cardCornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.cardElevation)
    ) {
        Column(modifier = Modifier.padding(Dimens.spaceMd)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.avatarSize)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = Dimens.spaceXs)
                            .width(90.dp)
                            .height(22.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = Dimens.spaceSm)
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .padding(top = Dimens.spaceMd)
                    .fillMaxWidth()
                    .height(Dimens.minTouchTarget)
                    .clip(RoundedCornerShape(Dimens.spaceSm))
                    .shimmerEffect()
            )
        }
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val base = MaterialTheme.colorScheme.surfaceVariant
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                base.copy(alpha = 0.6f),
                base.copy(alpha = 0.2f),
                base.copy(alpha = 0.6f),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}
