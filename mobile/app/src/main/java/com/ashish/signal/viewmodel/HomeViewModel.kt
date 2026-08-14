package com.ashish.signal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ashish.signal.data.model.ComponentDto
import com.ashish.signal.data.network.BffApiClient
import com.ashish.signal.data.repo.BffRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

/** State of the GET /screen/{screenId} load — distinct from the action-triggered overlay below. */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Success(val components: List<ComponentDto>) : HomeUiState
}

/**
 * Fetches the home screen on load and re-invokes the repo whenever a
 * component's action fires. Has no idea what a "token_card" or
 * "bottom_sheet" is — it only moves ComponentDto around.
 */
class HomeViewModel(
    private val screenId: String,
    private val repo: BffRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // The most recent component returned by an action, rendered as an overlay.
    // Generic — ComponentRenderer decides how (or whether) to display it.
    private val _overlayComponent = MutableStateFlow<ComponentDto?>(null)
    val overlayComponent: StateFlow<ComponentDto?> = _overlayComponent.asStateFlow()

    init {
        loadScreen()
    }

    fun retry() = loadScreen()

    private fun loadScreen() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = try {
                val response = repo.getScreen(screenId)
                HomeUiState.Success(response.components)
            } catch (e: IOException) {
                HomeUiState.Error(e.message ?: "Couldn't reach the server. Check your connection.")
            } catch (e: Exception) {
                HomeUiState.Error(e.message ?: "Something went wrong loading this screen.")
            }
        }
    }

    fun onAction(action: String, params: Map<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repo.postAction(action, params)
                _overlayComponent.value = response.component
            } catch (_: Exception) {
                // The BFF itself never errors on /action/explain (graceful fallback
                // is its job); this only catches a dead network. Silently drop —
                // there's nothing new to show the user beyond the tap not doing
                // anything, which is preferable to a jarring error for a
                // secondary interaction.
            }
        }
    }

    fun dismissOverlay() {
        _overlayComponent.value = null
    }

    companion object {
        /**
         * Builds the real repo here so callers (MainActivity, in this app)
         * only ever need a screenId — the UI layer has no reason to know
         * BffRepository or BffApiClient exist. Tests construct
         * HomeViewModel(screenId, fakeRepo) directly instead of going
         * through this factory.
         */
        fun factory(screenId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HomeViewModel(screenId, BffRepository(BffApiClient.api)) as T
            }
    }
}
