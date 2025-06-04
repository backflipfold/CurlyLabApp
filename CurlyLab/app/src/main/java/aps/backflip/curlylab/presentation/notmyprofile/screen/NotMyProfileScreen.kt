package aps.backflip.curlylab.presentation.notmyprofile.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordCard
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordLoading
import aps.backflip.curlylab.presentation.notmyprofile.viewmodel.NotMyProfileViewModel
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID

@Composable
fun NotMyProfileScreen(
    navController: NavController,
    viewModel: NotMyProfileViewModel = hiltViewModel(),
    userId: UUID?
) {
    val blogRecords by viewModel.blogRecords.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val subscribed by viewModel.subscribed.collectAsState()
    val subscribers by viewModel.subscribers.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadBlogRecords(it)
            viewModel.isSubscribed(it)
            viewModel.getProfileStats(it)
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
                userId?.let {
                    viewModel.loadBlogRecords(it)
                    viewModel.isSubscribed(it)
                    viewModel.getProfileStats(it)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .background(LightBeige)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, DarkGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (!imageUrl.isNullOrEmpty()) {
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = "Аватар пользователя",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Загрузить фото",
                                            tint = DarkGreen,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(24.dp))

                                Column {
                                    Text(
                                        text = userName,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = DarkGreen
                                    )
                                    Text(
                                        text = "Имя пользователя",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DarkGreen.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileStat(
                                    value = blogRecords.size.toString(),
                                    label = "Публикации"
                                )
                                ProfileStat(value = subscribers, label = "Подписчики")
                                ProfileStat(value = subscriptions, label = "Подписки")
                            }

                            OutlinedButton(
                                onClick = {
                                    if (userId != null) {
                                        if (subscribed) {
                                            viewModel.unsubscribe(userId)
                                        } else {
                                            viewModel.subscribe(userId)
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, DarkGreen),
                                shape = RoundedCornerShape(8.dp),
                                colors = if (subscribed) {
                                    ButtonDefaults.buttonColors(containerColor = Color.White)
                                } else {
                                    ButtonDefaults.buttonColors(containerColor = DarkGreen)
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (subscribed) {
                                            "Вы подписаны"
                                        } else {
                                            "Подписаться"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (subscribed) {
                                            DarkGreen
                                        } else {
                                            LightBeige
                                        }
                                    )
                                    Icon(
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                        imageVector = if (subscribed) {
                                            Icons.Default.Done
                                        } else {
                                            Icons.Default.AddCircle
                                        },
                                        contentDescription = null,
                                        tint = if (subscribed) {
                                            DarkGreen
                                        } else {
                                            LightBeige
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                when {
                    isLoading -> BlogRecordLoading()
                    error != null -> ErrorMessage(error ?: "Неизвестная ошибка")
                    else -> {
                        BlogRecordsList(
                            blogRecords = blogRecords,
                            navController = navController
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = DarkGreen.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BlogRecordsList(
    blogRecords: List<BlogRecord>,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        items(blogRecords) { blogRecord ->
            BlogRecordCard(
                blogRecord = blogRecord,
                modifier = Modifier
                    .fillMaxWidth(),
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