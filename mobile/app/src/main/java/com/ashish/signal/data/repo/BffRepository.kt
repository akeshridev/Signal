package com.ashish.signal.data.repo

import com.ashish.signal.data.model.ActionResponseDto
import com.ashish.signal.data.model.ScreenResponseDto
import com.ashish.signal.data.network.BffApi

/** Thin wrapper over BffApi. No business logic, no knowledge of component types. */
class BffRepository(private val api: BffApi) {

    suspend fun getScreen(screenId: String): ScreenResponseDto = api.getScreen(screenId)

    suspend fun postAction(action: String, params: Map<String, Any>): ActionResponseDto =
        api.postAction(action, params)
}
