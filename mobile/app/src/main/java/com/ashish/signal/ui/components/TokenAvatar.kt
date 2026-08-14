package com.ashish.signal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import coil.compose.SubcomposeAsyncImage
import com.ashish.signal.ui.theme.Dimens
import com.ashish.signal.ui.theme.card_border

/**
 * Shared token avatar: real logo via Coil, falling back to a monogram badge
 * while loading or on error (missing icon_url, dead URL, decode failure).
 * Used by both the token_card row and the bottom_sheet header so a tapped
 * card's icon carries through to the sheet it opens.
 */
@Composable
fun TokenAvatar(name: String, iconUrl: String?) {
    Box(
        modifier = Modifier
            .size(Dimens.avatarSize)
            .clip(CircleShape)
            .background(card_border),
        contentAlignment = Alignment.Center
    ) {
        if (iconUrl != null) {
            SubcomposeAsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.avatarSize)
                    .clip(CircleShape),
                loading = { Monogram(name) },
                error = { Monogram(name) }
            )
        } else {
            Monogram(name)
        }
    }
}

@Composable
private fun Monogram(name: String) {
    Text(
        text = name.take(1).uppercase(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}
