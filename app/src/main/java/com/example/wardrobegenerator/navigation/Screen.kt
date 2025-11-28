package com.example.wardrobegenerator.navigation

sealed class Screen(val route: String) {
    object Wardrobe : Screen("wardrobe")
    object AddItem : Screen("add_item")
    object Suggestions : Screen("suggestions")
    object SavedOutfits : Screen("saved_outfits")
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: Long) = "item_detail/$itemId"
    }
}
