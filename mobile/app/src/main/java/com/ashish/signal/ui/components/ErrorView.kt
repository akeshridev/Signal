package com.ashish.signal.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ashish.signal.ui.theme.Dimens

/**
 * Distinct from EmptyView: shown when GET /screen/{screenId} itself failed
 * (network/server error), not when it succeeded with zero components.
 */
@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.spaceLg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            // Decorative: the "Couldn't load this screen" text right below
            // already states the meaning, so a second announcement would be
            // redundant for TalkBack users.
            contentDescription = null,
            modifier = Modifier.size(Dimens.illustrationSize),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Couldn't load this screen",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.spaceMd)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.spaceSm)
        )
        val interactionSource = remember { MutableInteractionSource() }
        Button(
            onClick = onRetry,
            interactionSource = interactionSource,
            modifier = Modifier
                .padding(top = Dimens.spaceLg)
                .heightIn(min = Dimens.minTouchTarget)
                .pressScale(interactionSource)
        ) {
            Text(text = "Retry")
        }
    }
}
