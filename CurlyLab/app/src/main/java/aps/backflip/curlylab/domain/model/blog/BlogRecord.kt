package aps.backflip.curlylab.domain.model.blog

import java.time.OffsetDateTime
import java.util.UUID

data class BlogRecord(
    val recordId: UUID,
    val userId: UUID,
    val content: String,
    val tags: List<String>,
    var userName: String = "",
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)