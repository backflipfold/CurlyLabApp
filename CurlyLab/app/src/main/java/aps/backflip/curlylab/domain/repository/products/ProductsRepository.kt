package aps.backflip.curlylab.domain.repository.products

import aps.backflip.curlylab.data.remote.model.request.products.FavoriteRequest
import aps.backflip.curlylab.data.remote.model.request.products.ReviewRequest
import aps.backflip.curlylab.data.remote.model.response.products.ProductResponse
import aps.backflip.curlylab.data.remote.model.response.products.ReviewResponse
import java.util.UUID

interface ProductsRepository {
    suspend fun getProducts(): List<ProductResponse>
    suspend fun getProductById(productId: UUID): ProductResponse
    suspend fun getUserFavorites(userId: UUID): List<ProductResponse>
    suspend fun isFavorite(userId: UUID, productId: UUID): Boolean
    suspend fun addToFavorites(request: FavoriteRequest)
    suspend fun removeFromFavorites(request: FavoriteRequest)
    suspend fun getReviews(productId: UUID): List<ReviewResponse>
    suspend fun addReview(request: ReviewRequest)
    suspend fun updateReview(productId: UUID, userId: UUID, request: ReviewRequest)
    suspend fun deleteReview(userId: UUID, productId: UUID)
    suspend fun getCurrentUserId(): UUID
    suspend fun getCurrentUserName(): String
}