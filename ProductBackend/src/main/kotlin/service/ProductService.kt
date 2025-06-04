package org.example.service

import org.example.models.FavoriteInput
import org.example.models.ProductDTO
import org.example.models.ReviewDTO
import org.example.models.ReviewInput
import org.example.repository.ProductRepository
import java.util.UUID

class ProductService(
    private val repository: ProductRepository
) {
    fun getAllProducts(): List<ProductDTO> = repository.getAllProducts()
    fun getProductById(productId: UUID): ProductDTO = repository.getProductById(productId)
    fun addReview(review: ReviewInput) {
        require(review.rating in 1..5) { "Rating must be between 1 and 5" }
        repository.addReview(review)
    }

    fun getReviews(productId: UUID): List<ReviewDTO> = repository.getReviews(productId)
    fun addToFavorites(favorite: FavoriteInput) = repository.addToFavorites(favorite)
    fun removeFromFavorites(favorite: FavoriteInput) = repository.removeFromFavorites(favorite)
    fun getUserFavorites(userId: UUID): List<ProductDTO> = repository.getUserFavorites(userId)
    fun isFavorite(userId: UUID, productId: UUID): Boolean =
        repository.isFavorite(userId, productId)

    fun deleteReview(userId: UUID, productId: UUID) = repository.removeReview(userId, productId)
    fun updateReview(userId: UUID, productId: UUID, review: ReviewInput) {
        require(review.rating in 1..5) { "Rating must be between 1 and 5" }
        repository.updateReview(userId, productId, review)
    }
}