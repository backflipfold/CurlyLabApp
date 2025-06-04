package aps.backflip.curlylab.presentation.dictionary.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import aps.backflip.curlylab.domain.model.dictionary.Word
import aps.backflip.curlylab.presentation.dictionary.viewmodel.DictionaryViewModel
import aps.backflip.curlylab.ui.theme.DarkGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.filterWords(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Словарь",
                    style = MaterialTheme.typography.displayLarge,
                    color = DarkGreen
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    viewModel.filterWords(newValue)
                },
                label = { Text("Поиск") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            )

            LazyColumn {
                items(viewModel.words.value) { word ->
                    WordItem(word)
                }
            }
        }
    }
}

@Composable
fun WordItem(word: Word) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = word.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = word.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
