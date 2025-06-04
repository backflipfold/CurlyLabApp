package aps.backflip.curlylab.domain.model.products

import java.util.UUID

data class Favorite(
    val userId: UUID,
    val productId: UUID,
    val product: Product? = null
)