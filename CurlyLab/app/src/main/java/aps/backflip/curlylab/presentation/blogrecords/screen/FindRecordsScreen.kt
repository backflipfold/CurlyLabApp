package aps.backflip.curlylab.presentation.blogrecords.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordCard
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordLoading
import aps.backflip.curlylab.presentation.blogrecords.viewmodel.FindRecordsViewModel
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.LightGreen
import java.util.UUID

@Composable
fun FindRecordsScreen(
    viewModel: FindRecordsViewModel = hiltViewModel(),
    onBlogRecordClick: (UUID) -> Unit = {},
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val blogRecords by viewModel.blogRecords.collectAsState()

    LaunchedEffect(searchQuery) {
        viewModel.findPosts(searchQuery)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { navController.popBackStack() },
                    elevation = CardDefaults.elevatedCardElevation(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(16.dp),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = LightBeige
                    )
                }

                TextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        searchQuery = newValue
                        viewModel.findPosts(newValue)
                    },
                    label = { Text("Поиск") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LightGreen,
                        unfocusedContainerColor = DarkGreen,
                        focusedIndicatorColor = DarkGreen,
                        unfocusedIndicatorColor = LightGreen
                    )
                )
            }

            when {
                isLoading -> BlogRecordLoading()
                error != null -> ErrorMessage(error ?: "Неизвестная ошибка")
                else -> {
                    BlogRecordsList(
                        blogRecords = blogRecords,
                        onBlogRecordClick = onBlogRecordClick,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun BlogRecordsList(
    blogRecords: List<BlogRecord>,
    onBlogRecordClick: (UUID) -> Unit,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(blogRecords) { blogRecord ->
            BlogRecordCard(
                blogRecord = blogRecord,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBlogRecordClick(blogRecord.recordId) },
                isAuthor = false,
                navController = navController
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}