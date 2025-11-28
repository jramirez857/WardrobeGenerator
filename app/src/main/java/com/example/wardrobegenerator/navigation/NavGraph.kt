package com.example.wardrobegenerator.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun navGraph(navController: NavHostController, startDestination: String = Screen.Wardrobe.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Wardrobe.route) {
            // WardrobeScreen will be implemented in Phase 3
            placeholderScreen(screenName = "Wardrobe")
        }

        composable(Screen.AddItem.route) {
            // AddItemScreen will be implemented in Phase 2
            placeholderScreen(screenName = "Add Item")
        }

        composable(Screen.Suggestions.route) {
            // SuggestionsScreen will be implemented in Phase 4
            placeholderScreen(screenName = "Suggestions")
        }

        composable(Screen.SavedOutfits.route) {
            // SavedOutfitsScreen will be implemented later
            placeholderScreen(screenName = "Saved Outfits")
        }

        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: 0L
            // ItemDetailScreen will be implemented later
            placeholderScreen(screenName = "Item Detail: $itemId")
        }
    }
}

@Composable
private fun placeholderScreen(screenName: String) {
    androidx.compose.material3.Text(
        text = "Placeholder: $screenName",
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
    )
}
