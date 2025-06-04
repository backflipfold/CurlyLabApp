package aps.backflip.curlylab.domain.model.blogsubscriber

import java.util.UUID

data class BlogSubscriber (
    val blogSubscribersId: UUID,
    val authorId: UUID,
    val subscriberId: UUID
)