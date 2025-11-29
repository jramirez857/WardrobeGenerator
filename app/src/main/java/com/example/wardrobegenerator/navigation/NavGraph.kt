package com.example.wardrobegenerator.navigation

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wardrobegenerator.ui.additem.AddItemScreen
import com.example.wardrobegenerator.ui.additem.AddItemUiState
import com.example.wardrobegenerator.ui.additem.AddItemViewModel
import com.example.wardrobegenerator.ui.camera.CameraScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun navGraph(
    navController: NavHostController,
    startDestination: String = Screen.Wardrobe.route,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Wardrobe.route) {
            // WardrobeScreen will be implemented in Phase 3
            wardrobePlaceholder(
                paddingValues = paddingValues,
                onAddItemClick = { navController.navigate(Screen.Camera.route) }
            )
        }

        composable(Screen.Camera.route) {
            val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
            var permissionRequested by remember { mutableStateOf(false) }

            when {
                cameraPermissionState.status.isGranted -> {
                    CameraScreen(
                        paddingValues = paddingValues,
                        onImageCaptured = { uri ->
                            val encodedUri = Uri.encode(uri.toString())
                            navController.navigate(Screen.AddItem.createRoute(encodedUri))
                        },
                        onError = { error ->
                            // Handle camera error
                            navController.popBackStack()
                        }
                    )
                }
                cameraPermissionState.status.shouldShowRationale -> {
                    AlertDialog(
                        onDismissRequest = { navController.popBackStack() },
                        title = { Text("Camera Permission Required") },
                        text = { Text("This app needs camera access to take photos of your clothing items.") },
                        confirmButton = {
                            TextButton(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("Grant Permission")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { navController.popBackStack() }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                else -> {
                    LaunchedEffect(permissionRequested) {
                        if (!permissionRequested) {
                            cameraPermissionState.launchPermissionRequest()
                            permissionRequested = true
                        }
                    }
                }
            }
        }

        composable(
            route = Screen.AddItem.route,
            arguments = listOf(
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            val imageUri = Uri.parse(Uri.decode(encodedUri))
            val viewModel: AddItemViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            when (uiState) {
                is AddItemUiState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.popBackStack(Screen.Wardrobe.route, inclusive = false)
                        viewModel.resetState()
                    }
                }
                is AddItemUiState.Error -> {
                    val errorMessage = (uiState as AddItemUiState.Error).message
                    AlertDialog(
                        onDismissRequest = { viewModel.resetState() },
                        title = { Text("Error") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            TextButton(onClick = { viewModel.resetState() }) {
                                Text("OK")
                            }
                        }
                    )
                }
                else -> {
                    AddItemScreen(
                        imageUri = imageUri,
                        onSave = { name, category, color ->
                            viewModel.saveClothingItem(imageUri, name, category, color)
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
            }
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
private fun wardrobePlaceholder(paddingValues: PaddingValues, onAddItemClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItemClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Wardrobe (Phase 3)\nTap + to add an item",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
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
