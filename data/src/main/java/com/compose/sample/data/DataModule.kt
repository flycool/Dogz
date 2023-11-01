package com.compose.sample.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.compose.sample.data.breeds.BreedsDataStore
import com.compose.sample.data.breeds.BreedsRemoteApi
import com.compose.sample.data.breeds.BreedsRepositoryImpl
import com.compose.sample.data.favorites.FavoritesRepositoryImpl
import com.compose.sample.data.images.AppDatabase
import com.compose.sample.data.images.BreedImagesApi
import com.compose.sample.data.images.BreedImagesApiImpl
import com.compose.sample.data.images.DogImageDao
import com.compose.sample.data.images.ImagesRepositoryImpl
import com.compose.sample.domain.breeds.data.BreedsRepository
import com.compose.sample.domain.favorites.FavoritesRepository
import com.compose.sample.domain.images.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val Context.dataStore by preferencesDataStore("dog_breeds")

    @Provides
    @Singleton
    fun provideHttpClient(): KtorHttpClient = KtorHttpClient()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideBreedImagesApi(
        client: KtorHttpClient
    ): BreedImagesApi {
        return BreedImagesApiImpl(client)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)


    @Provides
    @Singleton
    fun provideDogImageDao(database: AppDatabase): DogImageDao =
        database.dogImageDao()

    @Provides
    @Singleton
    fun provideBreedRepository(
        breedsRemoteApi: BreedsRemoteApi,
        breedsDataStore: BreedsDataStore
    ): BreedsRepository {
        return BreedsRepositoryImpl(breedsRemoteApi, breedsDataStore)
    }

    @Provides
    @Singleton
    fun provideBreedImagesRepository(
        api: BreedImagesApi,
        dao: DogImageDao
    ): ImagesRepository {
        return ImagesRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        imageDao: DogImageDao
    ): FavoritesRepository {
        return FavoritesRepositoryImpl(imageDao)
    }

}