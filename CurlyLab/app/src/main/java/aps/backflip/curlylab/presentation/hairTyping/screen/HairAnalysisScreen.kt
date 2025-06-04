package aps.backflip.curlylab.presentation.hairTyping.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import aps.backflip.curlylab.presentation.navigation.Screen
import aps.backflip.curlylab.presentation.products.viewmodel.HairAnalysisViewModel
import aps.backflip.curlylab.ui.theme.*
import coil.compose.AsyncImage
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HairAnalysisScreen(
    viewModel: HairAnalysisViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val sheetExpanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Пришёл результат или ошибка — раскрываем шторку
    LaunchedEffect(result, error) {
        if (!result.isNullOrEmpty() || error != null) {
            bottomSheetState.bottomSheetState.expand()
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri

        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                viewModel.analyze(bytes)
            }
        }
    }

    BottomSheetScaffold(
        sheetPeekHeight = 48.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContainerColor = Color.White,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrightPink)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "≋ꕤ≋\uD80C\uDDB8≋ꕤ≋\uD80C\uDDB8≋ꕤ≋\uD80C\uDDB8≋ꕤ≋\uD80C\uDDB8≋ꕤ",
                    color = LightBeige,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = Golos,
                        fontSize = 28.sp
                    )
                )
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrightPink)
                    .padding(16.dp)
            ) {
                Text(
                    text = when {
                        isLoading -> "Ожидаем анализ…"
                        error != null -> "Ошибка: $error"
                        !result.isNullOrEmpty() -> "Тип волос: ${result?.uppercase()}"
                        else -> "Ожидаем загрузку фото"
                    },
                    color = LightBeige,
                    fontFamily = Golos,
                    fontSize = 18.sp
                )
                when{
                    !result.isNullOrEmpty() -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = {
                                    { navController.navigate(Screen.Main.route)
                                        navController.navigate(Screen.HairTyping.route)
                                    }
                                },
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = PurpleGrey40
                                )
                            ) {
                                Text(
                                    text = "Пока не сохранять",
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = Golos),
                                    color = PurpleGrey40
                                )
                            }
                            Button(
                                onClick = {
                                    viewModel.saveResult()
                                    navController.navigate(Screen.Main.route)
                                    navController.navigate(Screen.HairTyping.route)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey40)
                            )
                            {
                                Text(
                                    text = "Сохранить",
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = Golos),
                                    color = LightBeige
                                )
                            }
                        }
                    }
                }
            }
        },
        content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightBeige)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Нейросеть определит твой тип волос",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = Golos,
                    fontSize = 28.sp
                ),
                color = DarkGreen
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BrightPink)
            ) {
                Text(
                    text = """
                        Необходимо всего лишь загрузить фото, на котором хорошо видны волосы!
                        
                        Лучше всего подойдет снимок со спины :) 
                        Так нашей модели будет проще "разглядеть" волосы и точность предсказания будет выше.
                        
                        Ответ не заставит себя долго ждать :)
                    """.trimIndent(),
                    color = LightBeige02,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontFamily = Golos
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreen)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = LightPink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "загрузить фото",
                            color = DarkGreen,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 24.sp,
                                fontFamily = Golos
                            )
                        )
                    }
                }
            }

            selectedImageUri?.let { uri ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    )
}


