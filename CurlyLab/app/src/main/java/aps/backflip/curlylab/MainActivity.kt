package aps.backflip.curlylab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import aps.backflip.curlylab.presentation.navigation.RootNavGraph
import aps.backflip.curlylab.ui.theme.CurlyLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurlyLabTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    RootNavGraph(navController = navController)
                }
            }
        }
    }
}