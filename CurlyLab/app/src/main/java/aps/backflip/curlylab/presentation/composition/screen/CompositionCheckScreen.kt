package aps.backflip.curlylab.presentation.composition.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import aps.backflip.curlylab.presentation.composition.viewmodel.CompositionViewModel
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.BrightPink
import aps.backflip.curlylab.ui.theme.Golos

@Composable
fun CompositionCheckScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: CompositionViewModel = viewModel()

    val inputText by viewModel.inputText
    val result by viewModel.result.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBeige)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Проверка состава",
                style = MaterialTheme.typography.displayLarge,
                color = DarkGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { viewModel.onInputTextChange(it) },
                label = { Text("Введите или вставьте состав", fontFamily = Golos) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 10
            )

            imageUri?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Загрузить фото", color = Color.White, fontFamily = Golos)
                }

                Button(
                    onClick = {
                        viewModel.analyze(context, imageUri)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Проверить", color = Color.White, fontFamily = Golos)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (result.isNotBlank()) {
                Text(
                    result,
                    color = DarkGreen,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp),
                    fontFamily = Golos
                )
            }
        }

    }
}
