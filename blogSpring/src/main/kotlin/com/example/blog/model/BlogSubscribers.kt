package com.example.blog.model

import java.util.UUID

data class BlogSubscribers (
    val blogSubscribersId: UUID,
    val authorId: UUID,
    val subscriberId: UUID
)

