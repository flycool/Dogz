package com.compose.sample.domain.favorites

import com.compose.sample.domain.images.DogImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteImagesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {

    operator fun invoke(): Flow<List<DogImage>> {
        return favoritesRepository.favoriteImagesFlow
    }
}