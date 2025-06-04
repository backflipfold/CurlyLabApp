package aps.backflip.curlylab.models

import com.benasher44.uuid.Uuid
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class UserResponse(
    @Contextual val id: Uuid,
    val username: String,
    val language: String,
    val createdAt: Instant,
    val imageUrl: String? = null,
    val imageFileId: String? = null
)

@Serializable
data class UserRequest(
    val username: String? = null,
    val language: String? = "ru",
    val imageUrl: String? = null
)