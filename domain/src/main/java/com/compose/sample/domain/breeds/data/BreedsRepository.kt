package com.compose.sample.domain.breeds.data

import kotlinx.coroutines.flow.Flow

interface BreedsRepository {
    val breedsFlow: Flow<List<BreedInfo>>
}