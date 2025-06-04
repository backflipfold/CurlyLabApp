package aps.backflip.curlylab.presentation.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.products.FavoriteRequest
import aps.backflip.curlylab.data.remote.model.request.products.ReviewRequest
import aps.backflip.curlylab.data.remote.model.response.products.ReviewResponse
import aps.backflip.curlylab.domain.model.products.Product
import aps.backflip.curlylab.domain.repository.products.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted


@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites.asStateFlow()

    private val _reviews = MutableStateFlow<List<ReviewResponse>>(emptyList())
    val reviews: StateFlow<List<ReviewResponse>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedTab = MutableStateFlow(ProductsTab.ALL_PRODUCTS)
    val selectedTab: StateFlow<ProductsTab> = _selectedTab.asStateFlow()

    init {
        loadProducts()
        loadFavorites()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getProducts().map { it.toDomain() }
                }
                _products.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки товаров: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                val userId = withContext(Dispatchers.IO) { repository.getCurrentUserId() }
                val result = withContext(Dispatchers.IO) { repository.getUserFavorites(userId) }
                _favorites.value = result.map { it.toDomain() }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки избранного: ${e.message}"
                Log.e("Favorites", "Error to load favorite", e)

            }
        }
    }

    fun loadReviews(productId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getReviews(productId)
                }
                _reviews.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки отзывов: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(productId: UUID) {
        viewModelScope.launch {
            try {
                val userId = withContext(Dispatchers.IO) { repository.getCurrentUserId() }
                val request = FavoriteRequest(userId, productId)
                val existing = favorites.value.find { it.id == productId }

                if (existing != null) {
                    withContext(Dispatchers.IO) {
                        repository.removeFromFavorites(request)
                    }
                    _favorites.value = _favorites.value.filter { it.id != productId }
                } else {
                    withContext(Dispatchers.IO) {
                        repository.addToFavorites(request)
                        val addedProduct = repository.getProductById(productId).toDomain()
                        _favorites.value += addedProduct
                    }
                }
            } catch (e: Exception) {
                _error.value = "Не удалось обновить избранное: ${e.message}"
                Log.e("Favorites", "Error toggling favorite", e)
            }
        }
    }

    fun submitReview(request: ReviewRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    val alreadyExists = _reviews.value.any {
                        it.userId == request.userId && it.productId == request.productId
                    }
                    if (alreadyExists) {
                        repository.updateReview(request.userId, request.productId, request)
                    } else {
                        repository.addReview(request)
                    }
                }
                loadReviews(request.productId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка при отправке отзыва: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateReview(review: ReviewRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    repository.updateReview(
                        productId = review.productId,
                        userId = review.userId,
                        request = review
                    )
                }
                loadReviews(review.productId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка при обновлении отзыва: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteReview(userId: UUID, productId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteReview(productId, userId)
                }
                loadReviews(productId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка при удалении отзыва: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getCurrentUserId(): UUID {
        return withContext(Dispatchers.IO) {
            repository.getCurrentUserId()
        }
    }

    suspend fun getCurrentUsername(): String {
        return withContext(Dispatchers.IO) {
            repository.getCurrentUserName()
        }
    }

    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    private val _porosityTag = MutableStateFlow<String?>(null)
    private val _coloringTag = MutableStateFlow<String?>(null)
    private val _thicknessTag = MutableStateFlow<String?>(null)

    val porosityTag: StateFlow<String?> = _porosityTag.asStateFlow()
    val coloringTag: StateFlow<String?> = _coloringTag.asStateFlow()
    val thicknessTag: StateFlow<String?> = _thicknessTag.asStateFlow()
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _products,
        _porosityTag,
        _coloringTag,
        _thicknessTag
    ) { allProducts, porosity, coloring, thickness ->
        allProducts.filter { product ->
            val tags = product.tags.toSet()
            val porosityMatch = porosity == null || tags.contains(porosity)
            val coloringMatch = coloring == null || tags.contains(coloring)
            val thicknessMatch = thickness == null || tags.contains(thickness)
            porosityMatch && coloringMatch && thicknessMatch
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun toggleTag(category: String, tag: String) {
        when (category) {
            "Пористость" -> _porosityTag.value = if (_porosityTag.value == tag) null else tag
            "Окрашенность" -> _coloringTag.value = if (_coloringTag.value == tag) null else tag
            "Толщина" -> _thicknessTag.value = if (_thicknessTag.value == tag) null else tag
        }
    }


    fun selectTab(tab: ProductsTab) {
        _selectedTab.value = tab
    }

    fun clearAllFilters() {
        _porosityTag.value = null
        _coloringTag.value = null
        _thicknessTag.value = null
    }

}

enum class ProductsTab {
    ALL_PRODUCTS,
    FAVORITE_PRODUCTS
}