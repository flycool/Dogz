package com.compose.sample.ui.favorites

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.compose.sample.domain.images.DogImage
import com.compose.sample.ui.common.UiState
import com.compose.sample.ui.common.UiStateWrapper
import com.compose.sample.ui.images.DogImagesGrid

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.models.collectAsStateWithLifecycle()

    PureFavoritesScreen(
        state = uiState,
        navController = navController,
        onToggleSelectedBreed = viewModel::toggleBreedFilter,
        onToggleFavorite = viewModel::toggleFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PureFavoritesScreen(
    state: UiState<FavoritesModel>,
    navController: NavController,
    onToggleSelectedBreed: (ChipInfo) -> Unit,
    onToggleFavorite: (DogImage) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(navController = navController, scrollBehavior = scrollBehavior)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            UiStateWrapper(uiState = state) {
                if (it.dogImages.isEmpty()) {
                    EmptyScreen()
                } else {
                    Column {
                        BreedFilter(
                            breeds = it.filterChipsInfo,
                            onToggleSelectedBreed = onToggleSelectedBreed
                        )
                        DogImagesGrid(
                            images = it.dogImages,
                            onToggleFavorite = onToggleFavorite
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = {
            Text(
                text = "Favorites", maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedFilter(
    breeds: Set<ChipInfo>,
    onToggleSelectedBreed: (ChipInfo) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp)
    ) {
        breeds.forEach { breed ->
            FilterChip(
                selected = breed.selected,
                onClick = { onToggleSelectedBreed(breed) },
                label = { Text(text = breed.label) },
                trailingIcon = {
                    if (breed.selected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun EmptyScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No favorites yet")
    }
}

