package aps.backflip.curlylab.presentation.notmyprofile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.blogsubscriber.BlogSubscriberRequest
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.domain.repository.blogsubscriber.BlogSubscriberRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotMyProfileViewModel @Inject constructor(
    private val blogRecordRepository: BlogRecordRepository,
    private val blogSubscriberRepository: BlogSubscriberRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _blogRecords = MutableStateFlow<List<BlogRecord>>(emptyList())
    val blogRecords: StateFlow<List<BlogRecord>> = _blogRecords.asStateFlow()

    private val _subscribed = MutableStateFlow(false)
    val subscribed: StateFlow<Boolean> = _subscribed.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _subscribers = MutableStateFlow("")
    val subscribers: StateFlow<String> = _subscribers.asStateFlow()

    private val _subscriptions = MutableStateFlow("")
    val subscriptions: StateFlow<String> = _subscriptions.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    fun isSubscribed(userId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val subscriptionId = blogSubscriberRepository.getSubscriptionId(
                    BlogSubscriberRequest(
                        authorId = userId,
                        subscriberId = blogSubscriberRepository.getCurrentUserId(),
                        blogSubscribersId = UUID.randomUUID()
                    )

                )
                _subscribed.value = subscriptionId != null
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                Log.e("VIK", "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBlogRecords(userId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _blogRecords.value =
                    blogRecordRepository.findPostsByUser(userId).map { blogRecordResponse ->
                        BlogRecord(
                            content = blogRecordResponse.content,
                            recordId = blogRecordResponse.recordId,
                            userId = blogRecordResponse.userId,
                            userName = usersRepository.getUser(userId.toString()).username,
                            tags = blogRecordResponse.tags,
                            createdAt = blogRecordResponse.createdAt
                        )
                    }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun subscribe(authorId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _subscribed.value = blogSubscriberRepository.subscribe(
                    BlogSubscriberRequest(
                        authorId = authorId,
                        subscriberId = blogSubscriberRepository.getCurrentUserId(),
                        blogSubscribersId = UUID.randomUUID()
                    )
                )
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun unsubscribe(authorId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val subscriptionId = blogSubscriberRepository.getSubscriptionId(
                    BlogSubscriberRequest(
                        blogSubscribersId = UUID.randomUUID(),
                        subscriberId = blogSubscriberRepository.getCurrentUserId(),
                        authorId = authorId
                    )
                )
                if (subscriptionId != null) {
                    _subscribed.value = !blogSubscriberRepository.unsubscribe(subscriptionId)
                } else {
                    _subscribed.value = false
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getProfileStats(authorId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _subscribers.value = blogSubscriberRepository.subscribers(authorId).toString()
                _subscriptions.value = blogSubscriberRepository.subscriptions(authorId).toString()
                val user = usersRepository.getUser(authorId.toString())
                _userName.value = user.username
                _imageUrl.value = user.imageUrl
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}