package com.compose.sample.ui.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sample.domain.breeds.GetBreedsUseCase
import com.compose.sample.ui.common.UiState
import com.compose.sample.ui.common.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    getBreedsUseCase: GetBreedsUseCase
) : ViewModel() {

    val breedState = getBreedsUseCase().asUiState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )
}