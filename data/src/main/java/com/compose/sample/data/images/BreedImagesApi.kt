package com.compose.sample.data.images

import com.compose.sample.data.KtorHttpClient
import io.ktor.client.request.get
import javax.inject.Inject

const val BASE_URL = "https://dog.ceo/api/"

interface BreedImagesApi {
    suspend fun getBreedImages(breed: String): List<String>
}

class BreedImagesApiImpl @Inject constructor(
    private val client: KtorHttpClient
) : BreedImagesApi {

    override suspend fun getBreedImages(breed: String): List<String> {
        return client.safeApiCall {
            get("${BASE_URL}breed/$breed/images")
        }
    }
}