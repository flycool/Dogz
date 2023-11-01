package com.compose.sample.domain.favorites

import com.compose.sample.domain.images.DogImage
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoriteImagesFlow: Flow<List<DogImage>>

    suspend fun updateFavoriteStatus(url: String, isFavorite: Boolean)
}