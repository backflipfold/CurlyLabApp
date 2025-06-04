package aps.backflip.curlylab.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import aps.backflip.curlylab.presentation.blogrecords.screen.BlogRecordsScreen
import aps.backflip.curlylab.presentation.profile.screen.ProfileScreen
import aps.backflip.curlylab.presentation.home.screen.HomeScreen

@Composable
fun MainScreen(navController: NavHostController ) {
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(mainNavController) }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.BlogRecords.route) { BlogRecordsScreen(navController = navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController = navController) }
        }
    }
}
