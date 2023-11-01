package com.compose.sample.ui.breeds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.compose.sample.domain.breeds.BreedEntry
import com.compose.sample.domain.common.capitalizeWords
import com.compose.sample.ui.Screen
import com.compose.sample.ui.common.UiState
import com.compose.sample.ui.common.UiStateWrapper

@Composable
fun BreedsScreen(
    viewModel: BreedsViewModel = hiltViewModel(),
    navController: NavController
) {
    val breedListState by viewModel.breedState.collectAsStateWithLifecycle()

    BreedScreen(breedListState = breedListState, navigateToDogImages = { breedItem ->
        navController.navigate(Screen.DogImages(breedItem.route).route)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedScreen(
    breedListState: UiState<List<BreedEntry>>,
    navigateToDogImages: (BreedEntry) -> Unit,
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Dogiz",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            UiStateWrapper(uiState = breedListState) { data ->
                BreedList(breeds = data, onItemClick = navigateToDogImages)
            }
        }
    }
}

@Composable
fun BreedList(
    breeds: List<BreedEntry>,
    onItemClick: (BreedEntry) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(breeds) { breed ->
            BreedListItem(breed = breed, onClick = { onItemClick(breed) })
        }
    }
}


@Composable
fun BreedListItem(
    breed: BreedEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(text = breed.name.capitalizeWords())
            },
            modifier = Modifier.clickable(onClick = onClick),
            tonalElevation = 4.dp
        )
    }
}
