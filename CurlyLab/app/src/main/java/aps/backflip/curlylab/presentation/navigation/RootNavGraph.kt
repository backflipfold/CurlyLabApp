package aps.backflip.curlylab.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavGraph(navController: NavHostController) {
    var isLoggedIn by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.MainGraph.route else Screen.AuthGraph.route
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
    }
}