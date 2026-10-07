package com.metrolist.music
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavController
val LocalArtistNameAliases = compositionLocalOf<Map<String, String>> { emptyMap() }
val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }
