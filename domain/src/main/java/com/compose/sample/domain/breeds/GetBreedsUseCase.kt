package com.compose.sample.domain.breeds

import com.compose.sample.domain.breeds.data.BreedInfo
import com.compose.sample.domain.breeds.data.BreedsRepository
import com.compose.sample.domain.common.capitalizeWords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBreedsUseCase @Inject constructor(
    private val breedsRepository: BreedsRepository
) {

    operator fun invoke(): Flow<List<BreedEntry>> {
        return breedsRepository.breedsFlow.map(::mapInfoToItems)
    }

    private fun mapInfoToItems(
        breedInfoList: List<BreedInfo>
    ): List<BreedEntry> {
        return breedInfoList.flatMap { breed ->
            val subBreedItems = breed.subBreedsNames.map { subBreedName ->
                BreedEntry(
                    buildDisplayName(breed.name, subBreedName),
                    "${breed.name}_${subBreedName}"
                )
            }

            listOf(
                BreedEntry(breed.name, breed.name)
            ) + subBreedItems

        }
    }
}

fun buildDisplayName(breedName: String, subBreedName: String?): String {
    val capitalizedBreed = breedName.capitalizeWords()
    val capitalizedSubBreed = subBreedName?.capitalizeWords()

    return if (capitalizedSubBreed != null) {
        "$capitalizedBreed ($capitalizedSubBreed)"
    } else {
        capitalizedBreed
    }
}

fun buildDisplayNameFromKey(breedName: String): String {
    val parts = breedName.split('/')
    val breed = parts[0].capitalizeWords()
    val subBreed = parts.getOrNull(1)?.capitalizeWords()

    return buildDisplayName(breed, subBreed)
}