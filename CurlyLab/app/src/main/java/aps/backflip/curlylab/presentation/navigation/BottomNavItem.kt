package aps.backflip.curlylab.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import aps.backflip.curlylab.R

sealed class BottomNavItem(val icon: ImageVector, @StringRes val titleRes: Int, val route: String) {
    data object Home : BottomNavItem(Icons.Default.Home, R.string.home, Screen.Home.route)
    data object News : BottomNavItem(Icons.AutoMirrored.Filled.List, R.string.news, Screen.BlogRecords.route)
    data object Profile : BottomNavItem(Icons.Default.Person, R.string.profile, Screen.Profile.route)
}