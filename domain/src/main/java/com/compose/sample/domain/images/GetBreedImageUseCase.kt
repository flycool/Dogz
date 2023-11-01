package com.compose.sample.domain.images

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetBreedImageUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    operator fun invoke(breedKey: String): Flow<List<DogImage>> {
        return imagesRepository.getImagesByBreed(breedKey).distinctUntilChanged()
    }
}