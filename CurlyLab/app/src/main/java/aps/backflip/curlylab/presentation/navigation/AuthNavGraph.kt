package aps.backflip.curlylab.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import aps.backflip.curlylab.presentation.auth.screen.ResetPasswordScreen
import aps.backflip.curlylab.presentation.auth.screen.SignInScreen
import aps.backflip.curlylab.presentation.auth.screen.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.SignIn.route,
        route = Screen.AuthGraph.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToResetPassword = {
                    navController.navigate(Screen.ResetPassword.route)
                }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onResetSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                }
            )
        }
    }
}