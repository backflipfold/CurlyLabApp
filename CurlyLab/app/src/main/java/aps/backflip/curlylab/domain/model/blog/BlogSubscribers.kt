package aps.backflip.curlylab.domain.model.blog

import java.util.UUID

data class BlogSubscribers (
    val blogSubscribersId: UUID,
    val authorId: UUID,
    val subscriberId: UUID
)