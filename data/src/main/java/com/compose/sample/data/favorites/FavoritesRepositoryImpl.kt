package com.compose.sample.data.favorites

import com.compose.sample.data.images.DogImageDao
import com.compose.sample.data.images.DogImageDataEntity.Companion.toDogImageEntity
import com.compose.sample.domain.favorites.FavoritesRepository
import com.compose.sample.domain.images.DogImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val imagesDao: DogImageDao
) : FavoritesRepository {
    override val favoriteImagesFlow: Flow<List<DogImage>> = imagesDao.getFavoriteDogImages().map {
        it.map {
            it.toDogImageEntity()
        }
    }

    override suspend fun updateFavoriteStatus(url: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            imagesDao.updateFavoriteStatus(url, isFavorite)
        }
    }
}