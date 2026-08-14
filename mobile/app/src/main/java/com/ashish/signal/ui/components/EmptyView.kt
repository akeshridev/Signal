package com.ashish.signal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ashish.signal.ui.theme.Dimens

/**
 * Distinct from ErrorView: shown when GET /screen/{screenId} succeeded but
 * returned zero components, not when the request itself failed.
 */
@Composable
fun EmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.spaceLg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Inbox,
            // Decorative: the text below already states the meaning.
            contentDescription = null,
            modifier = Modifier.size(Dimens.illustrationSize),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Nothing here yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.spaceMd)
        )
        Text(
            text = "Check back later for token updates.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.spaceSm)
        )
    }
}
