package com.ashish.signal.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ashish.signal.ui.components.ComponentRenderer
import com.ashish.signal.ui.components.EmptyView
import com.ashish.signal.ui.components.ErrorView
import com.ashish.signal.ui.components.TokenCardSkeleton
import com.ashish.signal.ui.theme.Dimens
import com.ashish.signal.viewmodel.HomeUiState
import com.ashish.signal.viewmodel.HomeViewModel

private const val SKELETON_COUNT = 3

/**
 * Renders whatever list of components the view model hands it. Has zero
 * knowledge of what a "token_card" or "bottom_sheet" even is — that lives
 * entirely in ComponentRenderer. The only thing this screen owns is which
 * of the four load states (loading/error/empty/content) to show, which is
 * a screen-level UX concern, not a component-tree concern.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val overlayComponent by viewModel.overlayComponent.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "home-state") { state ->
            when (state) {
                is HomeUiState.Loading -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(SKELETON_COUNT) { TokenCardSkeleton() }
                    }
                }

                is HomeUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = viewModel::retry,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is HomeUiState.Success -> {
                    if (state.components.isEmpty()) {
                        EmptyView(modifier = Modifier.fillMaxSize())
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = Dimens.spaceSm)
                        ) {
                            items(state.components) { component ->
                                ComponentRenderer(component = component, onAction = viewModel::onAction)
                            }
                        }
                    }
                }
            }
        }
    }

    overlayComponent?.let { component ->
        ComponentRenderer(
            component = component,
            onAction = viewModel::onAction,
            onDismiss = viewModel::dismissOverlay
        )
    }
}
