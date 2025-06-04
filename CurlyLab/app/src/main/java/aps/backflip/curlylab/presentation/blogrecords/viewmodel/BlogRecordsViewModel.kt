package aps.backflip.curlylab.presentation.blogrecords.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.domain.repository.blogsubscriber.BlogSubscriberRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BlogRecordsViewModel @Inject constructor(
    private val repository: BlogRecordRepository,
    private val usersRepository: UsersRepository,
    private val blogSubscriberRepository: BlogSubscriberRepository
) : ViewModel() {

    private val _blogRecords = MutableStateFlow<List<BlogRecord>>(emptyList())
    val blogRecords: StateFlow<List<BlogRecord>> = _blogRecords.asStateFlow()

    private val _recommendedBlogRecords = MutableStateFlow<List<BlogRecord>>(emptyList())
    val recommendedBlogRecords: StateFlow<List<BlogRecord>> = _recommendedBlogRecords.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedTab = MutableStateFlow(Tab.SUBSCRIPTIONS)
    val selectedTab: StateFlow<Tab> = _selectedTab.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadBlogRecords()
        loadRecommendedRecords()
    }

    private fun loadBlogRecords() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = getCurrentUserId()
                if (blogSubscriberRepository.subscriptions(userId) == 0){
                    _blogRecords.value = emptyList<BlogRecord>()
                } else {
                    _blogRecords.value =
                        repository.getPostsBySubscribedUsers(userId)
                            .map { blogRecordResponse ->
                                BlogRecord(
                                    content = blogRecordResponse.content,
                                    recordId = blogRecordResponse.recordId,
                                    userId = blogRecordResponse.userId,
                                    tags = blogRecordResponse.tags,
                                    createdAt = blogRecordResponse.createdAt,
                                    userName = usersRepository.getUser(blogRecordResponse.userId.toString()).username
                                )
                            }
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _recommendedBlogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getCurrentUserId(): UUID {
        return withContext(Dispatchers.IO) {
            usersRepository.getCurrentUserId()
        }
    }

    private fun loadRecommendedRecords() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recommendedBlogRecords.value =
                    repository.getRecommendedPosts()
                        .map { blogRecordResponse ->
                            BlogRecord(
                                content = blogRecordResponse.content,
                                recordId = blogRecordResponse.recordId,
                                userId = blogRecordResponse.userId,
                                tags = blogRecordResponse.tags,
                                createdAt = blogRecordResponse.createdAt,
                                userName =  usersRepository.getUser(blogRecordResponse.userId.toString()).username
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


    fun selectTab(tab: Tab) {
        _selectedTab.value = tab
    }

    fun refresh() {
        loadBlogRecords()
        loadRecommendedRecords()
    }
}

enum class Tab {
    SUBSCRIPTIONS,
    RECOMMENDATIONS
}