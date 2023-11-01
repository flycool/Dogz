package com.compose.sample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentManager.BackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.sample.ui.breeds.BreedsScreen
import com.compose.sample.ui.favorites.FavoriteCountBadge
import com.compose.sample.ui.favorites.FavoritesScreen
import com.compose.sample.ui.images.ImagesScreen
import com.compose.sample.ui.theme.DogBreedsTheme

sealed class Screen(val route: String) {
    data object BreedsList : Screen("breedsList")
    data object Favorites : Screen("favorites")
    class DogImages(breed: String) : Screen("dogImages/$breed")
}

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.BreedsList.route) {
        composable(Screen.BreedsList.route) {
            BreedsScreen(navController = navController)
        }
        composable(
            route = "dogImages/{breed}",
            arguments = listOf(navArgument("breed") { type = NavType.StringType })
        ) { backStateEntry ->
            val breedArg = backStateEntry.arguments?.getString("breed")
            if (breedArg != null) {
                val (breed, subBreed) = breedArg.split("_").let {
                    it[0] to it.getOrNull(1)
                }

                ImagesScreen(
                    navController = navController,
                    breed = breed,
                    subBreed = subBreed
                )
            }
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    DogBreedsTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MainAppScreen(navController = navController)
        }
    }
}

@Composable
fun MainAppScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    val navBaskStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBaskStackEntry?.destination?.route

                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text(text = "Breeds") },
                        selected = currentRoute == Screen.BreedsList.route,
                        onClick = {
                            navController.navigate(Screen.BreedsList.route) {
                                popUpTo(Screen.BreedsList.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )

                    NavigationBarItem(
                        icon = {
                            FavoriteCountBadge()
                        },
                        label = { Text(text = "Favorites") },
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            MyNavHost(navController = navController)
        }
    }
}