package com.compose.sample.data.images

import com.compose.sample.data.images.DogImageDataEntity.Companion.fromDogImageEntity
import com.compose.sample.data.images.DogImageDataEntity.Companion.toDogImageEntity
import com.compose.sample.domain.breeds.buildDisplayName
import com.compose.sample.domain.breeds.buildDisplayNameFromKey
import com.compose.sample.domain.images.DogImage
import com.compose.sample.domain.images.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesRepositoryImpl @Inject constructor(
    private val dogBreedApi: BreedImagesApi,
    private val imagesDataStore: DogImageDao
) : ImagesRepository {

    override fun getImagesByBreed(breedKey: String): Flow<List<DogImage>> =
        getImagesByBreedFromLocal(breedKey)
            .onStart { fetchAndSave(breedKey) }
            .distinctUntilChanged()

    private suspend fun fetchAndSave(breedKey: String) {
        val remoteData = getRemoteBreedImages(breedKey)
        imagesDataStore.insertAll(remoteData.map { it.fromDogImageEntity() })
    }

    private suspend fun getRemoteBreedImages(breedKey: String): List<DogImage> {
        return dogBreedApi.getBreedImages(breedKey).map { url ->
            DogImage(
                breedName = buildDisplayNameFromKey(breedKey),
                isFavorite = false,
                url = url,
                breedKey = breedKey
            )
        }
    }

    private fun getImagesByBreedFromLocal(breedKey: String): Flow<List<DogImage>> {
        return imagesDataStore.getDogImagesByBreedKey(breedKey).map {
            it.map { entity ->
                entity.toDogImageEntity()
            }
        }
    }
}