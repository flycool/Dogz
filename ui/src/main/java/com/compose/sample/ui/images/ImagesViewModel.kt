package com.compose.sample.ui.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sample.domain.images.DogImage
import com.compose.sample.domain.images.GetBreedImageUseCase
import com.compose.sample.domain.images.buildBreedKey
import com.compose.sample.ui.common.UiState
import com.compose.sample.ui.common.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val getBreedImageUseCase: GetBreedImageUseCase
) : ViewModel() {

    private val _dogImagesState =
        MutableStateFlow<UiState<List<DogImage>>>(UiState.Loading)
    val dogImagesState: StateFlow<UiState<List<DogImage>>> get() = _dogImagesState

    private var lastFetchJob: Job? = null

    fun fetchDogImages(breed: String, subBreed: String?) {
        lastFetchJob?.cancel()

        val breedKey = buildBreedKey(subBreed, breed)

        lastFetchJob = viewModelScope.launch(Dispatchers.IO) {
            getBreedImageUseCase(breedKey).asUiState()
                .collect { state ->
                    _dogImagesState.value = state
                }
        }
    }
}