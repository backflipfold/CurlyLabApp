package aps.backflip.curlylab.data.remote.model.response.blog

import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.utils.serializers.OffsetDateTimeSerializer
import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class BlogRecordResponse(
    @Serializable(with = UUIDSerializer::class)
    val recordId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val content: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createdAt: OffsetDateTime,
    val tags: List<String>
) {
    fun toDomain() = BlogRecord(
        recordId = recordId,
        userId = userId,
        content = content,
        createdAt = createdAt,
        tags = tags
    )
}