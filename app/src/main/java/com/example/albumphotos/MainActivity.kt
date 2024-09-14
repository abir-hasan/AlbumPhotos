package com.example.albumphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.albumphotos.navigation.Albums
import com.example.albumphotos.navigation.Photos
import com.example.albumphotos.ui.album.list.AlbumsScreen
import com.example.albumphotos.ui.album.photos.PhotosScreen
import com.example.albumphotos.ui.theme.AlbumPhotosTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlbumPhotosTheme {
                val navHostController = rememberNavController()
                AlbumPhotosNavHost(navHostController)
            }
        }
    }
}

@Composable
private fun AlbumPhotosNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Albums,
        modifier = modifier,
    ) {
        composable<Albums> {
            AlbumsScreen(navHostController)
        }
        composable<Photos> {
            val albumId = it.toRoute<Photos>().albumId
            PhotosScreen(albumId)
        }
    }
}
