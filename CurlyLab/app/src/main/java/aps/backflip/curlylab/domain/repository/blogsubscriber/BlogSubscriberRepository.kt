package aps.backflip.curlylab.domain.repository.blogsubscriber

import aps.backflip.curlylab.data.remote.model.request.blogsubscriber.BlogSubscriberRequest
import java.util.UUID


interface BlogSubscriberRepository {
    suspend fun subscriptions(userId: UUID): Int
    suspend fun subscribers(userId: UUID): Int
    suspend fun subscribe(blogSubscriberRequest: BlogSubscriberRequest): Boolean
    suspend fun unsubscribe(id: UUID): Boolean
    suspend fun getCurrentUserId(): UUID
    suspend fun getSubscriptionId(blogSubscriberRequest: BlogSubscriberRequest): UUID?
}