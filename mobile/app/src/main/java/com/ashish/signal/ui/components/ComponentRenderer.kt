package com.ashish.signal.ui.components

import androidx.compose.runtime.Composable
import com.ashish.signal.data.model.ComponentDto

/**
 * The only place in the app that branches on component type. Screens and the
 * view model never do this — they just pass ComponentDto through.
 *
 * Adding a new component type means: add it to shared/component-schema.json,
 * add a case here, and write the *Component.kt that renders it. Nothing else
 * in the app changes.
 */
@Composable
fun ComponentRenderer(
    component: ComponentDto,
    onAction: (action: String, params: Map<String, Any>) -> Unit,
    onDismiss: () -> Unit = {}
) {
    when (component.type) {
        "token_card" -> TokenCardComponent(data = component.data, onAction = onAction)
        "bottom_sheet" -> BottomSheetComponent(data = component.data, onDismiss = onDismiss)
        else -> Unit // Unknown component type — skip it rather than crash.
    }
}
