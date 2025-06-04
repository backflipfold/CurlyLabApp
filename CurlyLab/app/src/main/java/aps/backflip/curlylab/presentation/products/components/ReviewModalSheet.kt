package aps.backflip.curlylab.presentation.products.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import aps.backflip.curlylab.R
import aps.backflip.curlylab.data.remote.model.request.products.ReviewRequest
import aps.backflip.curlylab.domain.model.products.Product
import aps.backflip.curlylab.domain.model.products.Review
import aps.backflip.curlylab.presentation.products.viewmodel.ProductsViewModel
import aps.backflip.curlylab.ui.theme.AccentPink
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.LightGreen
import aps.backflip.curlylab.ui.theme.LightPink
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReviewModalSheet(
    product: Product,
    onDismiss: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val scope = rememberCoroutineScope()
    val modalState = rememberModalBottomSheetState()
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var editingReview by remember { mutableStateOf<Review?>(null) }

    var userId by remember { mutableStateOf<UUID?>(null) }
    var username by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        userId = viewModel.getCurrentUserId()
        username = viewModel.getCurrentUsername()
        viewModel.loadReviews(product.id)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalState,
        modifier = modifier,
        containerColor = LightBeige
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightPink),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            placeholder = painterResource(R.drawable.curly_wave),
                            error = painterResource(R.drawable.curly_wave)
                        )
                    }

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = if (editingReview != null) "Редактировать отзыв" else "Оставить отзыв",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkGreen
                    )

                    Spacer(Modifier.height(16.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (i in 1..5) {
                            IconButton(
                                onClick = { rating = i },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (i <= rating) R.drawable.pink_star
                                        else R.drawable.grey_star_border
                                    ),
                                    contentDescription = "Оценка $i",
                                    tint = if (i <= rating) AccentPink else Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Ваш отзыв") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AccentPink,
                            unfocusedBorderColor = DarkGreen,
                            focusedLabelColor = DarkGreen,
                            unfocusedLabelColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    if (editingReview != null) {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    editingReview = null
                                    rating = 0
                                    comment = ""
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, DarkGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Отменить", color = DarkGreen)
                            }

                            Button(
                                onClick = {
                                    editingReview?.let { review ->
                                        viewModel.deleteReview(review.userId, review.productId)
                                        editingReview = null
                                        rating = 0
                                        comment = ""
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentPink,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Удалить")
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (userId != null && username != null) {
                                scope.launch {
                                    if (editingReview != null) {
                                        viewModel.updateReview(
                                            ReviewRequest(
                                                productId = product.id,
                                                userId = userId!!,
                                                userName = username!!,
                                                rating = rating,
                                                comment = comment
                                            )
                                        )
                                    } else {
                                        viewModel.submitReview(
                                            ReviewRequest(
                                                productId = product.id,
                                                userId = userId!!,
                                                userName = username!!,
                                                rating = rating,
                                                comment = comment
                                            )
                                        )
                                    }
                                    editingReview = null
                                    rating = 0
                                    comment = ""
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGreen,
                            contentColor = Color.White
                        ),
                        enabled = rating > 0 && comment.isNotBlank() && userId != null
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(if (editingReview != null) "Обновить" else "Отправить")
                        }
                    }

                    error?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Все отзывы",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkGreen
                    )
                    Icon(
                        painter = painterResource(R.drawable.pink_star),
                        contentDescription = null,
                        tint = DarkGreen.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            when {
                isLoading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(color = AccentPink)
                    }
                }

                reviews.isEmpty() -> {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = "Пока нет отзывов",
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(reviews) { review ->
                            val isCurrentUserReview = review.userId == userId
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                            ) {
                                ReviewItem(
                                    review = review.toDomain(),
                                    showMenu = isCurrentUserReview,
                                    onEditClick = if (isCurrentUserReview) {
                                        {
                                            editingReview = review.toDomain()
                                            rating = review.rating
                                            comment = review.comment
                                        }
                                    } else null,
                                    onDeleteClick = if (isCurrentUserReview) {
                                        {
                                            viewModel.deleteReview(review.userId, review.productId)
                                        }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReviewItem(
    review: Review,
    showMenu: Boolean,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.userName,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
                Text(
                    text = review.createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        painter = painterResource(
                            if (i <= review.rating) R.drawable.pink_star
                            else R.drawable.grey_star_border
                        ),
                        contentDescription = null,
                        tint = if (i <= review.rating) AccentPink else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))


            if (showMenu) {
                Box {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Действия",
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .zIndex(1f)
                            .background(LightBeige.copy(alpha = 0.95f))
                    ) {
                        onEditClick?.let { onClick ->
                            DropdownMenuItem(
                                text = { Text("Редактировать") },
                                onClick = {
                                    onClick()
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = DarkGreen
                                    )
                                }
                            )
                        }
                        onDeleteClick?.let { onClick ->
                            DropdownMenuItem(
                                text = { Text("Удалить", color = AccentPink) },
                                onClick = {
                                    onClick()
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = AccentPink
                                    )
                                }
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = review.comment,
            color = Color.DarkGray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}