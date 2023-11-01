package com.compose.sample.domain.favorites

import com.compose.sample.domain.images.DogImage
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(breedImage: DogImage) {
        favoritesRepository.updateFavoriteStatus(breedImage.url, !breedImage.isFavorite)
    }
}