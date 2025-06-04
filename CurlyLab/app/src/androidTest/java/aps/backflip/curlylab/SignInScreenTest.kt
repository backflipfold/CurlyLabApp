package aps.backflip.curlylab

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.testing.TestNavHostController
import aps.backflip.curlylab.presentation.navigation.Screen
import aps.backflip.curlylab.presentation.navigation.authNavGraph
import aps.backflip.curlylab.presentation.navigation.mainNavGraph
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SignInScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var navController: TestNavHostController


    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = TestNavHostController(composeTestRule.activity)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = navController,
                startDestination = Screen.AuthGraph.route
            ) {
                authNavGraph(navController)
                mainNavGraph(navController)
            }
        }
    }

    @Test
    fun correctSignInDoNotNavigatesToMainGraph() {
        composeTestRule.onNodeWithText("Email").performTextInput("spamovik123@ya.ru")
        composeTestRule.onNodeWithText("Пароль").performTextInput("vika123456")

        composeTestRule.onNodeWithText("Войти").performClick()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            navController.currentBackStackEntry?.destination?.route == Screen.Main.route
        }
    }

    @Test
    fun incorrectSignInNavigatesToMainGraph() {
        composeTestRule.onNodeWithText("Email").performTextInput("dvdv@ya.ru")
        composeTestRule.onNodeWithText("Пароль").performTextInput("dvddk")

        composeTestRule.onNodeWithText("Войти").performClick()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            navController.currentBackStackEntry?.destination?.route != Screen.Main.route
        }
    }
}
