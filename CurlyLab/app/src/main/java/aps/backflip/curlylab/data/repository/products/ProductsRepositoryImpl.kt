package aps.backflip.curlylab.data.repository.products

import android.content.Context
import aps.backflip.curlylab.data.local.preferences.AuthManager
import aps.backflip.curlylab.data.remote.api.ApiService
import aps.backflip.curlylab.data.remote.model.request.products.FavoriteRequest
import aps.backflip.curlylab.data.remote.model.request.products.ReviewRequest
import aps.backflip.curlylab.data.remote.model.response.products.ProductResponse
import aps.backflip.curlylab.data.remote.model.response.products.ReviewResponse
import aps.backflip.curlylab.domain.repository.products.ProductsRepository
import java.util.UUID
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ProductsRepository {

    override suspend fun getProducts(): List<ProductResponse> {
        return apiService.getProducts()
    }

    override suspend fun getProductById(productId: UUID): ProductResponse {
        return apiService.getProductById(productId)
    }

    override suspend fun getUserFavorites(userId: UUID): List<ProductResponse> {
        return apiService.getUserFavorites(userId)
    }

    override suspend fun isFavorite(userId: UUID, productId: UUID): Boolean =
        apiService.isFavorite(userId, productId)

    override suspend fun addToFavorites(request: FavoriteRequest) =
        apiService.addToFavorites(request)

    override suspend fun removeFromFavorites(request: FavoriteRequest) =
        apiService.removeFromFavorites(request)

    override suspend fun getReviews(productId: UUID): List<ReviewResponse> {
        return apiService.getReviews(productId)
    }

    override suspend fun addReview(request: ReviewRequest) =
        apiService.addReview(request)

    override suspend fun updateReview(productId: UUID, userId: UUID, request: ReviewRequest) =
        apiService.updateReview(productId, userId, request)

    override suspend fun deleteReview(userId: UUID, productId: UUID) =
        apiService.deleteReview(userId, productId)

    override suspend fun getCurrentUserId(): UUID {
        val id = AuthManager.getUserId(context)
            ?: throw IllegalStateException("User ID not available")
        return UUID.fromString(id)
    }

    override suspend fun getCurrentUserName(): String {
        return AuthManager.getUsername(context)
            ?: throw IllegalStateException("User name not available")
    }
}
