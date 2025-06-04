package com.example.blog.model

import java.time.OffsetDateTime
import java.util.*

data class BlogRecord (
    val recordId: UUID,
    val userId: UUID,
    val content: String,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val tags: List<String>
)