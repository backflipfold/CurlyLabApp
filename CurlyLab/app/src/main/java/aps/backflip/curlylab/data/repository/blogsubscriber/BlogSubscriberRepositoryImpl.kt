package aps.backflip.curlylab.data.repository.blogsubscriber

import android.content.Context
import aps.backflip.curlylab.data.local.preferences.AuthManager
import aps.backflip.curlylab.data.remote.api.ApiService
import aps.backflip.curlylab.data.remote.model.request.blogsubscriber.BlogSubscriberRequest
import aps.backflip.curlylab.domain.repository.blogsubscriber.BlogSubscriberRepository
import java.util.UUID
import javax.inject.Inject

class BlogSubscriberRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : BlogSubscriberRepository {
    override suspend fun subscriptions(userId: UUID): Int {
        return apiService.subscriptions(userId)
    }

    override suspend fun subscribers(userId: UUID): Int {
        return apiService.subscribers(userId)
    }

    override suspend fun subscribe(blogSubscriberRequest: BlogSubscriberRequest): Boolean {
        return apiService.subscribe(blogSubscriberRequest)
    }

    override suspend fun unsubscribe(id: UUID): Boolean {
        return apiService.unsubscribe(id)
    }

    override suspend fun getCurrentUserId(): UUID {
        val id = AuthManager.getUserId(context)
            ?: throw IllegalStateException("User ID not available")
        return UUID.fromString(id)
    }

    override suspend fun getSubscriptionId(blogSubscriberRequest: BlogSubscriberRequest): UUID? {
        return apiService.getSubscriptionId(
                blogSubscriberRequest
            )?.blogSubscribersId
    }
}