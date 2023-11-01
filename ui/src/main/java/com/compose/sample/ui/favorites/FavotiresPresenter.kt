package com.compose.sample.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.compose.sample.domain.images.DogImage
import com.compose.sample.ui.common.UiState
import com.compose.sample.ui.common.asUiState
import com.compose.sample.ui.common.mapSuccess
import kotlinx.coroutines.flow.Flow

data class ChipInfo(
    val label: String,
    val selected: Boolean
)

data class FavoritesModel(
    val dogImages: List<DogImage>,
    val filterChipsInfo: Set<ChipInfo>
)

sealed interface Event {
    data class ToggleSelectedBreed(val breed: ChipInfo) : Event
}

@Composable
fun favoritesPresenter(
    events: Flow<Event>,
    favoriteImagesFlow: Flow<List<DogImage>>
): UiState<FavoritesModel> {
    var favoriteImagesResult by remember {
        mutableStateOf<UiState<Collection<DogImage>>>(UiState.Loading)
    }
    var filterBreeds by remember {
        mutableStateOf(emptySet<String>())
    }

    LaunchedEffect(Unit) {
        favoriteImagesFlow.asUiState()
            .collect { images ->
                favoriteImagesResult = images
                if (images is UiState.Success) {
                    val chipsLabels = images.data.map { it.breedName }.toSet()
                    filterBreeds = filterBreeds.intersect(chipsLabels)
                }
            }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            if (event is Event.ToggleSelectedBreed) {
                val toggleBreed = event.breed
                filterBreeds = if (toggleBreed.selected) {
                    filterBreeds - toggleBreed.label
                } else {
                    filterBreeds + toggleBreed.label
                }
            }
        }
    }

    return favoriteImagesResult.mapSuccess { favoriteImages ->
        val chipsLabels = favoriteImages.map { it.breedName }.toSet()
        val filteredImages = favoriteImages.filter {
            filterBreeds.isEmpty() || it.breedName in filterBreeds
        }

        val filterChipsInfo = chipsLabels.map { label ->
            ChipInfo(label = label, selected = label in filterBreeds)
        }.toSet()

        FavoritesModel(
            dogImages = filteredImages,
            filterChipsInfo = filterChipsInfo
        )
    }
}
