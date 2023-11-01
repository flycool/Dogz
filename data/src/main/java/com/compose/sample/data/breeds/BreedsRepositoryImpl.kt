package com.compose.sample.data.breeds

import com.compose.sample.domain.breeds.data.BreedInfo
import com.compose.sample.domain.breeds.data.BreedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedsRepositoryImpl @Inject constructor(
    private val breedsApi: BreedsRemoteApi,
    private val breedsDataStore: BreedsDataStore
) : BreedsRepository {
    override val breedsFlow: Flow<List<BreedInfo>> = flow {
        //try fetching from local fist
        val localBreeds = getBreedsFromLocal()
        if (localBreeds != null) {
            emit(localBreeds)
        }

        // then always fetch from remote
        try {
            val remoteBreeds = breedsApi.getAllBreeds()
            val breeds = BreedInfoImpl.fromMap(remoteBreeds)
            breedsDataStore.save(breeds)
            emit(breeds)
        } catch (e: Exception) {
            if (localBreeds == null) {
                throw e
            }
        }
    }

    private suspend fun getBreedsFromLocal(): List<BreedInfoImpl>? {
        return breedsDataStore.get.firstOrNull()
    }

}