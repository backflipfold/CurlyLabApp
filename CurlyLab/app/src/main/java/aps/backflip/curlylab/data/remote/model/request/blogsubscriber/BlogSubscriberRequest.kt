package aps.backflip.curlylab.data.remote.model.request.blogsubscriber

import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BlogSubscriberRequest(
    @Serializable(with = UUIDSerializer::class)
    val blogSubscribersId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val authorId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val subscriberId: UUID
)