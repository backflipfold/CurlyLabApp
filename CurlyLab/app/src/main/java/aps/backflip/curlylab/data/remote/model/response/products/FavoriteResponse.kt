package aps.backflip.curlylab.data.remote.model.response.products

import aps.backflip.curlylab.domain.model.products.Favorite
import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FavoriteResponse(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    val product: ProductResponse? = null
) {
    fun toDomain() = Favorite(
        userId = userId,
        productId = productId,
        product = product?.toDomain()
    )
}