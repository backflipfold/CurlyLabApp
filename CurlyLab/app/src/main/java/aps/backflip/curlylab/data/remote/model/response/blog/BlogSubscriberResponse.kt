package aps.backflip.curlylab.data.remote.model.response.blog

import aps.backflip.curlylab.domain.model.blogsubscriber.BlogSubscriber
import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BlogSubscriberResponse(
    @Serializable(with = UUIDSerializer::class)
    val blogSubscribersId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val subscriberId: UUID,
) {
    fun toDomain() = BlogSubscriber(
        blogSubscribersId = blogSubscribersId,
        authorId = authorId,
        subscriberId = subscriberId
    )
}