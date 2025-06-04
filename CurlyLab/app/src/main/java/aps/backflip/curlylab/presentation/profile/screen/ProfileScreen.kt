package aps.backflip.curlylab.presentation.profile.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import aps.backflip.curlylab.R
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordCard
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordLoading
import aps.backflip.curlylab.presentation.navigation.Screen
import aps.backflip.curlylab.presentation.profile.viewmodel.ProfileViewModel
import aps.backflip.curlylab.ui.theme.AccentPink
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.LightGreen
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.OffsetDateTime
import java.util.UUID

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val blogRecords by viewModel.blogRecords.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val subscribers by viewModel.subscribers.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val hairType by viewModel.hairType.collectAsState()

    var isDialogEditOpen by remember { mutableStateOf(false) }
    var isDialogCreateOpen by remember { mutableStateOf(false) }
    var currentBlogRecord by remember { mutableStateOf<BlogRecord?>(null) }
    var expanded by remember { mutableStateOf(false) }


    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .background(LightBeige)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
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

                                ProfileImagePicker(
                                    profileViewModel = viewModel
                                )

                                Spacer(modifier = Modifier.width(24.dp))

                                Column {
                                    Text(
                                        text = userName ?: "Имя",
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

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                OutlinedButton(
                                    onClick = {
                                        isDialogCreateOpen = true
                                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                            if (!task.isSuccessful) {
                                                Log.w(
                                                    "VIK",
                                                    "Fetching FCM registration token failed",
                                                    task.exception
                                                )
                                                return@addOnCompleteListener
                                            }

                                            val token = task.result
                                            Log.w("VIK", "token: ${token}")


                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(1.dp, DarkGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Создать пост",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = DarkGreen
                                        )
                                        Icon(
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            imageVector = Icons.Default.Create,
                                            contentDescription = null,
                                            tint = DarkGreen
                                        )
                                    }
                                }

                                Box {
                                    IconButton(
                                        onClick = { expanded = true },
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .width(48.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "More options",
                                            tint = DarkGreen,
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .border(
                                                    1.dp,
                                                    DarkGreen,
                                                    RoundedCornerShape(8.dp)
                                                )
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        modifier = Modifier
                                            .zIndex(1f)
                                            .background(LightBeige.copy(alpha = 0.95f))
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Выйти") },
                                            onClick = {
                                                viewModel.logout()
                                                navController.navigate(Screen.AuthGraph.route)
                                                expanded = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                                    contentDescription = null,
                                                    tint = DarkGreen
                                                )
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text("Удалить аккаунт") },
                                            onClick = {
                                                viewModel.deleteAccount()
                                                navController.navigate(Screen.AuthGraph.route)
                                                expanded = false
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = DarkGreen
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HairTypeCard(R.drawable.porosity, hairType?.porosity?.capitalize() ?: "Пористость")
                        HairTypeCard(R.drawable.thin, hairType?.thickness?.capitalize() ?: "Толщина")
                        HairTypeCard(
                            R.drawable.color, when (hairType?.isColored) {
                                true -> "Окрашенные"
                                false -> "Неокрашенные"
                                else -> "Окрашенность"
                            }
                        )
                    }
                }

                when {
                    isLoading -> BlogRecordLoading()
                    error != null -> ErrorMessage(error ?: "Неизвестная ошибка")
                    else -> {
                        BlogRecordsList(
                            blogRecords = blogRecords,
                            onEdit = { blogRecord ->
                                currentBlogRecord = blogRecord
                                isDialogEditOpen = true
                            },
                            onDelete = { id ->
                                viewModel.deleteBlogPost(id)
                            },
                            navController = navController
                        )
                    }
                }

                if (isDialogEditOpen && currentBlogRecord != null) {
                    EditPostDialog(
                        blogRecord = currentBlogRecord!!,
                        onDismiss = {
                            isDialogEditOpen = false
                            currentBlogRecord = null
                        },
                        onSave = { newContent ->
                            viewModel.editBlogPost(newContent.recordId, newContent)
                        }
                    )
                }
                if (isDialogCreateOpen) {
                    CreatePostDialog(
                        onDismiss = {
                            isDialogCreateOpen = false
                        },
                        onSave = { newContent ->
                            viewModel.addBlogPost(newContent)
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun ProfileImagePicker(
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val imageUrl by profileViewModel.imageUrl.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes()
            inputStream?.close()
            fileBytes?.let {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
                val part = MultipartBody.Part.createFormData("image", "avatar.jpg", requestFile)
                profileViewModel.uploadUserImage(part)
            }
        }
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .clickable { launcher.launch("image/*") }
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
}

@Composable
private fun HairTypeCard(icon: Int, text: String) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            AsyncImage(
                model = icon,
                contentDescription = "icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxHeight()
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = DarkGreen.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
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
    onEdit: (BlogRecord) -> Unit,
    onDelete: (UUID) -> Unit,
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        blogRecords.forEach { blogRecord ->
            BlogRecordCard(
                blogRecord = blogRecord,
                modifier = Modifier
                    .fillMaxWidth(),
                isAuthor = true,
                onEdit = { onEdit(blogRecord) },
                onDelete = { onDelete(blogRecord.recordId) },
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditPostDialog(
    blogRecord: BlogRecord,
    onDismiss: () -> Unit = {},
    onSave: (BlogRecord) -> Unit = {}
) {
    var text by remember { mutableStateOf(blogRecord.content) }
    var tags by remember { mutableStateOf(blogRecord.tags) }
    var newTag by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .clickable { onDismiss() },
                        elevation = CardDefaults.elevatedCardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkGreen),
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = LightBeige
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Редактировать пост",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = {
                            Text(
                                "Содержимое поста",
                                color = DarkGreen.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkGreen,
                            unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                            focusedIndicatorColor = LightGreen,
                            unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = AccentPink.copy(alpha = 0.2f),
                                    labelColor = AccentPink
                                ),
                                border = null,
                                modifier = Modifier.height(24.dp)
                            )

                            IconButton(
                                onClick = {
                                    val tagsCopy = tags.toMutableList()
                                    tagsCopy.remove(tag)
                                    tags = tagsCopy
                                },
                                modifier = Modifier
                                    .size(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Удалить тег"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = newTag,
                            onValueChange = { newTag = it },
                            label = {
                                Text(
                                    "Добавить тег",
                                    color = DarkGreen.copy(alpha = 0.6f)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedLabelColor = DarkGreen,
                                unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                                focusedIndicatorColor = LightGreen,
                                unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (newTag.isNotBlank()) {
                                    val tagsCopy = tags.toMutableList()
                                    tagsCopy.add(newTag)
                                    tags = tagsCopy
                                    newTag = ""
                                }
                            },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Добавить тег"
                            )
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                onSave(
                                    BlogRecord(
                                        recordId = blogRecord.recordId,
                                        content = text,
                                        tags = tags,
                                        userId = blogRecord.userId,
                                        createdAt = blogRecord.createdAt
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonColors(
                                containerColor = DarkGreen,
                                contentColor = Color.White,
                                disabledContentColor = DarkGreen,
                                disabledContainerColor = Color.White
                            )
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit = {},
    onSave: (BlogRecord) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var newTag by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .clickable { onDismiss() },
                        elevation = CardDefaults.elevatedCardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkGreen),
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = LightBeige
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Редактировать пост",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = {
                            Text(
                                "Содержимое поста",
                                color = DarkGreen.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkGreen,
                            unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                            focusedIndicatorColor = LightGreen,
                            unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = AccentPink.copy(alpha = 0.2f),
                                    labelColor = AccentPink
                                ),
                                border = null,
                                modifier = Modifier.height(24.dp)
                            )

                            IconButton(
                                onClick = {
                                    val tagsCopy = tags.toMutableList()
                                    tagsCopy.remove(tag)
                                    tags = tagsCopy
                                },
                                modifier = Modifier
                                    .size(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Удалить тег"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = newTag,
                            onValueChange = { newTag = it },
                            label = {
                                Text(
                                    "Добавить тег",
                                    color = DarkGreen.copy(alpha = 0.6f)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedLabelColor = DarkGreen,
                                unfocusedLabelColor = DarkGreen.copy(alpha = 0.6f),
                                focusedIndicatorColor = LightGreen,
                                unfocusedIndicatorColor = DarkGreen.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (newTag.isNotBlank()) {
                                    val tagsCopy = tags.toMutableList()
                                    tagsCopy.add(newTag)
                                    tags = tagsCopy
                                    newTag = ""
                                }
                            },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Добавить тег"
                            )
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                onSave(
                                    BlogRecord(
                                        recordId = UUID.randomUUID(),
                                        content = text,
                                        tags = tags,
                                        userId = UUID.randomUUID(),
                                        createdAt = OffsetDateTime.now()
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonColors(
                                containerColor = DarkGreen,
                                contentColor = Color.White,
                                disabledContentColor = DarkGreen,
                                disabledContainerColor = Color.White
                            )
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
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