package com.compose.sample.data.images

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DogImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(dogImages: List<DogImageDataEntity>)

    @Query("select * from dog_images where  breed_key = :breedKey")
    fun getDogImagesByBreedKey(breedKey: String): Flow<List<DogImageDataEntity>>

    @Query("select * from dog_images where  is_favorite = 1")
    fun getFavoriteDogImages(): Flow<List<DogImageDataEntity>>

    @Query("update dog_images set is_favorite=:isFavorite where url = :url")
    fun updateFavoriteStatus(url: String, isFavorite: Boolean): Int
}
