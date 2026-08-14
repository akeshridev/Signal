package com.ashish.signal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ashish.signal.ui.theme.Dimens
import com.ashish.signal.ui.theme.card_background
import com.ashish.signal.ui.theme.card_border
import com.ashish.signal.ui.theme.crypto_gain
import com.ashish.signal.ui.theme.crypto_loss
import kotlin.math.absoluteValue

/**
 * Renders the token_card component type from shared/component-schema.json.
 * [data] is read defensively — the schema is the contract, not the compiler.
 *
 * Flat, low-glare card: solid dark fill + hairline border (no shadow/tonal
 * glow) per the reference design — name+ticker leading, price+change
 * trailing, compact white "Ask AI" pill bottom-right rather than a
 * full-width button.
 */
@Composable
fun TokenCardComponent(
    data: Map<String, Any>,
    onAction: (action: String, params: Map<String, Any>) -> Unit
) {
    val name = data["name"] as? String ?: return
    val symbol = data["symbol"] as? String ?: ""
    val priceDisplay = data["price_display"] as? String ?: ""
    val change24h = (data["change_24h"] as? Number)?.toDouble()
    val iconUrl = data["icon_url"] as? String
    val rank = (data["rank"] as? Number)?.toInt()

    @Suppress("UNCHECKED_CAST")
    val actionButton = data["ai_action_button"] as? Map<String, Any>
    val actionLabel = actionButton?.get("label") as? String
    val actionName = actionButton?.get("action") as? String
    @Suppress("UNCHECKED_CAST")
    val actionParams = (actionButton?.get("params") as? Map<String, Any>) ?: emptyMap()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.cardSpacing, horizontal = Dimens.screenPadding),
        shape = RoundedCornerShape(Dimens.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = card_background),
        border = BorderStroke(1.dp, card_border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.spaceMd)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Groups icon + name + price into one TalkBack announcement so
                    // the card reads as "SOL, $142.31" rather than three separate
                    // stops; the action button below stays its own focus stop.
                    .semantics(mergeDescendants = true) {},
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TokenAvatar(name = name, iconUrl = iconUrl)

                    Column(
                        modifier = Modifier.padding(start = Dimens.spaceMd)
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (symbol.isNotBlank()) {
                                Text(
                                    text = symbol,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (rank != null) {
                                Surface(
                                    color = card_border,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(start = Dimens.spaceXs)
                                ) {
                                    Text(
                                        text = "#$rank",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = priceDisplay,
                        style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = "tnum"),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (change24h != null) {
                        val isUp = change24h >= 0
                        Text(
                            text = "${if (isUp) "+" else "-"}${"%.1f".format(change24h.absoluteValue)}%",
                            style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
                            fontWeight = FontWeight.Medium,
                            color = if (isUp) crypto_gain else crypto_loss
                        )
                    }
                }
            }

            if (actionLabel != null && actionName != null) {
                val interactionSource = remember { MutableInteractionSource() }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.spaceMd),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onAction(
                                actionName,
                                actionParams + ("token" to name) + ("icon_url" to (iconUrl ?: ""))
                            )
                        },
                        interactionSource = interactionSource,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = Dimens.spaceMd),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(Dimens.askAiButtonHeight)
                            .pressScale(interactionSource)
                            .semantics { contentDescription = actionLabel }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Ask AI",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = Dimens.spaceSm)
                        )
                    }
                }
            }
        }
    }
}
