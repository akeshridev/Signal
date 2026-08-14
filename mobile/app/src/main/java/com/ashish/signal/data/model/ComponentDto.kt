package com.ashish.signal.data.model

/**
 * Generic component-tree node matching shared/component-schema.json.
 *
 * [data] is deliberately untyped — adding a new component type on the BFF
 * (a new entry in shared/component-schema.json) never requires a DTO change
 * here. Only ui/components/ComponentRenderer.kt needs to learn what a new
 * `type` means.
 */
data class ComponentDto(
    val type: String,
    val data: Map<String, Any>
)

/** Response shape for GET /screen/{screenId}. */
data class ScreenResponseDto(
    val screen_id: String,
    val components: List<ComponentDto>
)

/** Response shape for POST /action/{action}. */
data class ActionResponseDto(
    val component: ComponentDto
)
