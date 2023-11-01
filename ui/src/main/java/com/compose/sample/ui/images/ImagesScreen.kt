package com.compose.sample.ui.images

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.compose.sample.domain.breeds.buildDisplayName
import com.compose.sample.domain.common.capitalizeWords
import com.compose.sample.ui.common.UiStateWrapper
import com.compose.sample.ui.favorites.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    navController: NavController,
    breed: String,
    subBreed: String?
) {
    val imagesViewModel: ImagesViewModel = hiltViewModel()
    val favoritesViewModel:FavoritesViewModel = hiltViewModel()
    val dogImageState by imagesViewModel.dogImagesState.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(key1 = breed) {
        imagesViewModel.fetchDogImages(breed, subBreed)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = buildDisplayName(breed, subBreed).capitalizeWords(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            UiStateWrapper(uiState = dogImageState) { dogImageList ->
                DogImagesGrid(images = dogImageList, onToggleFavorite = { dogImage ->
                    favoritesViewModel.toggleFavorite(dogImage)
                })
            }
        }
    }
}