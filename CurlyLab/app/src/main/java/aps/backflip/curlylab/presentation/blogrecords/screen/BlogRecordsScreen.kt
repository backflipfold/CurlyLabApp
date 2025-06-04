package aps.backflip.curlylab.presentation.blogrecords.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordCard
import aps.backflip.curlylab.presentation.blogrecords.components.BlogRecordLoading
import aps.backflip.curlylab.presentation.blogrecords.viewmodel.BlogRecordsViewModel
import aps.backflip.curlylab.presentation.blogrecords.viewmodel.Tab
import aps.backflip.curlylab.presentation.navigation.Screen
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.UUID
import com.google.accompanist.swiperefresh.SwipeRefresh

@Composable
fun BlogRecordsScreen(
    viewModel: BlogRecordsViewModel = hiltViewModel(),
    onBlogRecordClick: (UUID) -> Unit = {},
    navController: NavController
) {
    val blogRecords by viewModel.blogRecords.collectAsState()
    val recommendedRecords by viewModel.recommendedBlogRecords.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Новости",
                style = MaterialTheme.typography.displayLarge,
                color = DarkGreen,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SegmentedControl(
                    modifier = Modifier.weight(1f),
                    items = listOf("Подписки", "Рекомендации"),
                    defaultSelectedItemIndex = if (selectedTab == Tab.SUBSCRIPTIONS) 0 else 1,
                    onItemSelection = { selectedIndex ->
                        when (selectedIndex) {
                            0 -> viewModel.selectTab(Tab.SUBSCRIPTIONS)
                            1 -> viewModel.selectTab(Tab.RECOMMENDATIONS)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                ElevatedCard(
                    modifier = Modifier
                        .clickable { navController.navigate(Screen.FindRecords.route) },
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = LightBeige
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> BlogRecordLoading()
                error != null -> ErrorMessage(error ?: "Неизвестная ошибка")
                else -> {
                    val recordsToShow =
                        if (selectedTab == Tab.SUBSCRIPTIONS) blogRecords else recommendedRecords
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing),
                        onRefresh = { viewModel.refresh() }
                    ) {
                        BlogRecordsList(
                            blogRecords = recordsToShow,
                            onBlogRecordClick = onBlogRecordClick,
                            navController = navController
                        )
                    }
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
fun SegmentedControl(
    modifier: Modifier = Modifier,
    items: List<String> = listOf("Рекомендации", "Подписки"),
    defaultSelectedItemIndex: Int = 0,
    cornerRadius: Int = 24,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableIntStateOf(defaultSelectedItemIndex) }

    Card(
        modifier = modifier
            .height(38.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items.forEachIndexed { index, item ->
                Card(
                    modifier = modifier
                        .weight(1f)
                        .padding(2.dp)
                        .clickable {
                            selectedIndex.value = index
                            onItemSelection(selectedIndex.value)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedIndex.value == index) {
                            DarkGreen
                        } else {
                            LightBeige
                        },
                        contentColor = if (selectedIndex.value == index)
                            LightBeige
                        else
                            DarkGreen
                    ),
                    shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStartPercent = cornerRadius,
                            bottomStartPercent = cornerRadius
                        )

                        items.size - 1 -> RoundedCornerShape(
                            topEndPercent = cornerRadius,
                            bottomEndPercent = cornerRadius
                        )

                        else -> RoundedCornerShape(0.dp)
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            style = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                fontWeight = if (selectedIndex.value == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedIndex.value == index) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center
                        )
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