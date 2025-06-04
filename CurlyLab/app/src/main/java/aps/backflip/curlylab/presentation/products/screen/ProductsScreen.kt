package aps.backflip.curlylab.presentation.products.screen

import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aps.backflip.curlylab.domain.model.products.Product
import aps.backflip.curlylab.presentation.blogrecords.screen.SegmentedControl
import aps.backflip.curlylab.presentation.products.components.ProductCard
import aps.backflip.curlylab.presentation.products.components.ProductsLoading
import aps.backflip.curlylab.presentation.products.components.ReviewModalSheet
import aps.backflip.curlylab.presentation.products.viewmodel.ProductsTab
import aps.backflip.curlylab.presentation.products.viewmodel.ProductsViewModel
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.BrightPink
import aps.backflip.curlylab.ui.theme.Golos
import java.util.UUID
import aps.backflip.curlylab.presentation.products.components.TagFilterSheet


@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val porosityTag by viewModel.porosityTag.collectAsState()
    val coloringTag by viewModel.coloringTag.collectAsState()
    val thicknessTag by viewModel.thicknessTag.collectAsState()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isFilterVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "База средств",
                style = MaterialTheme.typography.displayLarge,
                color = DarkGreen,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SegmentedControl(
                    modifier = Modifier.weight(1f),
                    items = listOf("Все продукты", "Избранное"),
                    defaultSelectedItemIndex = if (selectedTab == ProductsTab.ALL_PRODUCTS) 0 else 1,
                    onItemSelection = { selectedIndex ->
                        when (selectedIndex) {
                            0 -> viewModel.selectTab(ProductsTab.ALL_PRODUCTS)
                            1 -> viewModel.selectTab(ProductsTab.FAVORITE_PRODUCTS)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                ElevatedCard(
                    modifier = Modifier.clickable { isFilterVisible = !isFilterVisible },
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BrightPink)
                ) {
                    Text(
                        text = "⋮",
                        color = LightBeige,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = Golos)
                    )
                }
            }

            AnimatedVisibility(
                visible = isFilterVisible,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(LightBeige, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .padding(16.dp)
                ) {
                TagFilterSheet(
                        selectedTags = mapOf(
                            "Пористость" to porosityTag,
                            "Окрашенность" to coloringTag,
                            "Толщина" to thicknessTag
                        ),
                        onTagClick = { category, tag -> viewModel.toggleTag(category, tag) },
                        onClose = { isFilterVisible = false }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ElevatedCard(
                            onClick = {
                                viewModel.clearAllFilters()
                                isFilterVisible = false
                            },
                            colors = CardDefaults.cardColors(containerColor = BrightPink)
                        ) {
                            Text(
                                text = "Сбросить фильтры",
                                color = LightBeige,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = Golos)
                            )
                        }

                        ElevatedCard(
                            onClick = { isFilterVisible = false },
                            colors = CardDefaults.cardColors(containerColor = DarkGreen)
                        ) {
                            Text(
                                text = "Сохранить",
                                color = LightBeige,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = Golos)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> ProductsLoading()
                error != null -> ErrorMessage(error ?: "Неизвестная ошибка")
                else -> {
                    val productsToShow =
                        if (selectedTab == ProductsTab.ALL_PRODUCTS) filteredProducts else favorites

                    ProductsList(
                        productsToShow = productsToShow,
                        favorites = favorites.map { it.id },
                        onProductClick = { product ->
                            selectedProduct = product
                            viewModel.loadReviews(product.id)
                        },
                        onFavoriteClick = { productId ->
                            viewModel.toggleFavorite(productId)
                        }
                    )
                }
            }
        }

        selectedProduct?.let { product ->
            ReviewModalSheet(
                product = product,
                onDismiss = { selectedProduct = null },
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProductsList(
    productsToShow: List<Product>,
    favorites: List<UUID>,
    onFavoriteClick: (UUID) -> Unit,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productsToShow) { product ->
            ProductCard(
                product = product,
                isFavorite = favorites.contains(product.id),
                onFavoriteClick = { onFavoriteClick(product.id) },
                onProductClick = { onProductClick(product) },
                modifier = Modifier.fillMaxWidth()
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
