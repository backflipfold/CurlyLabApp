package aps.backflip.curlylab.presentation.profile.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import aps.backflip.curlylab.R

@Composable
fun SettingsScreen(navController: NavController) {
    Text(text = stringResource(id = R.string.settings))
}