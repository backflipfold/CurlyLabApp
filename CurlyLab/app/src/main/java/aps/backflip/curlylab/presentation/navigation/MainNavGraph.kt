package aps.backflip.curlylab.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import aps.backflip.curlylab.presentation.blogrecords.screen.BlogRecordsScreen
import aps.backflip.curlylab.presentation.blogrecords.screen.FindRecordsScreen
import aps.backflip.curlylab.presentation.dictionary.screen.DictionaryScreen
import aps.backflip.curlylab.presentation.hairTyping.screen.HairTypingScreen
import aps.backflip.curlylab.presentation.hairTyping.screen.HairAnalysisScreen
import aps.backflip.curlylab.presentation.products.screen.ProductsScreen
import aps.backflip.curlylab.presentation.guide.screen.GuideScreen
import aps.backflip.curlylab.presentation.composition.screen.CompositionCheckScreen
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.screens.IsColoredTextTypingScreen
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.screens.PorosityTextTypingScreen
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.screens.ThicknessTextTypingScreen
import aps.backflip.curlylab.presentation.notmyprofile.screen.NotMyProfileScreen
import java.util.UUID


fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Main.route,
        route = Screen.MainGraph.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.Dictionary.route) {
            DictionaryScreen()
        }
        composable(Screen.Products.route) {
            ProductsScreen()
        }
        composable(Screen.Guide.route) {
            GuideScreen(navController)
        }
        composable(Screen.HairTyping.route) {
            HairTypingScreen(navController)
        }
        composable(Screen.BlogRecords.route){
            BlogRecordsScreen(navController = navController)
        }
        composable(Screen.FindRecords.route) {
            FindRecordsScreen(navController = navController)
        }
        composable(Screen.CompositionCheck.route) {
            CompositionCheckScreen(navController)
        }
        composable(Screen.NotMyProfile.route){ backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("id")
            NotMyProfileScreen(userId = UUID.fromString(itemId), navController = navController)
        }
        composable(Screen.HairAnalysisRoute.route) {
            HairAnalysisScreen(navController = navController)
        }
        composable(Screen.PorosityTextTyping.route) {
            PorosityTextTypingScreen(navController = navController)
        }

        composable(Screen.ThicknessTextTyping.route) {
            ThicknessTextTypingScreen(navController = navController)
        }

        composable(Screen.IsColoredTextTyping.route) {
            IsColoredTextTypingScreen(navController = navController)
        }
    }
}