package aps.backflip.curlylab.presentation.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.blog.BlogRecordRequest
import aps.backflip.curlylab.data.local.preferences.AuthManager
import aps.backflip.curlylab.data.remote.model.response.profile.HairTypeResponse
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.domain.repository.auth.AuthRepository
import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.domain.repository.blogsubscriber.BlogSubscriberRepository
import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: BlogRecordRepository,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val blogSubscriberRepository: BlogSubscriberRepository,
    private val hairTypeRepository: HairTypesRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _blogRecords = MutableStateFlow<List<BlogRecord>>(emptyList())
    val blogRecords: StateFlow<List<BlogRecord>> = _blogRecords.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _logoutState = MutableStateFlow<Result<Unit>?>(null)
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)

    private val _subscribers = MutableStateFlow("")
    val subscribers: StateFlow<String> = _subscribers.asStateFlow()

    private val _subscriptions = MutableStateFlow("")
    val subscriptions: StateFlow<String> = _subscriptions.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _hairType = MutableStateFlow<HairTypeResponse?>(null)
    val hairType: StateFlow<HairTypeResponse?> = _hairType.asStateFlow()

    init {
        loadHairType()
        loadBlogRecords()
        loadProfile()
        getNumSubscribers()
        getNumSubscriptions()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val userId = usersRepository.getCurrentUserId().toString()
                val user =
                    withContext(Dispatchers.IO) { usersRepository.getUser(userId) }
                _userName.value = user.username
                _imageUrl.value = user.imageUrl
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки данных профиля: ${e.message}"
            }
        }
    }

    private fun loadHairType() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = usersRepository.getCurrentUserId().toString()
                val hairType = hairTypeRepository.getHairType(userId)
                _hairType.value = hairType
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки типа волос: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadBlogRecords() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _blogRecords.value =
                    repository.findPostsByUser(usersRepository.getCurrentUserId())
                        .map { blogRecordResponse ->
                            BlogRecord(
                                content = blogRecordResponse.content,
                                recordId = blogRecordResponse.recordId,
                                userId = blogRecordResponse.userId,
                                userName = getCurrentUsername(),
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

    fun deleteBlogPost(recordId: UUID) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deletePost(recordId)
            if (result) {
                _blogRecords.value = _blogRecords.value.filterNot { it.recordId == recordId }
            } else {
                _error.value = "Не удалось удалить запись"
            }
            _isLoading.value = false
        }
    }

    fun addBlogPost(blogRecord: BlogRecord) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val newRecord = repository.addPost(
                    BlogRecordRequest(
                        recordId = blogRecord.recordId,
                        content = blogRecord.content,
                        createdAt = blogRecord.createdAt,
                        userId = getCurrentUserId(),
                        tags = blogRecord.tags
                    )
                )
                if (newRecord) {
                    blogRecord.userName = getCurrentUsername()
                    _blogRecords.value = listOf(blogRecord) + _blogRecords.value
                } else {
                    _error.value = "Не удалось добавить пост"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun editBlogPost(recordId: UUID, newContent: BlogRecord) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val updatedRecord = repository.editPost(
                    recordId,
                    BlogRecordRequest(
                        recordId = newContent.recordId,
                        content = newContent.content,
                        createdAt = newContent.createdAt,
                        userId = newContent.userId,
                        tags = newContent.tags
                    )
                )
                if (updatedRecord != null) {
                    _blogRecords.value = _blogRecords.value.map { record ->
                        updatedRecord.userName = usersRepository.getCurrentUserName()
                        if (record.recordId == recordId) updatedRecord else record
                    }
                } else {
                    _error.value = "Не удалось обновить пост"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun retryUntilImageChanges(
        oldUrl: String,
        maxRetries: Int = 5,
        delayMillis: Long = 1000
    ) {
        repeat(maxRetries) {
            delay(delayMillis)

            try {
                val userId = usersRepository.getCurrentUserId()
                val user =
                    withContext(Dispatchers.IO) { usersRepository.getUser(userId.toString()) }

                if (!user.imageUrl.isNullOrBlank() && user.imageUrl != oldUrl) {
                    _imageUrl.value = user.imageUrl
                    return
                }

            } catch (e: Exception) {
                Log.w("Profile", "Ошибка при повторной проверке фото: ${e.message}")
            }
        }
        _error.value = "Не удалось получить обновлённую фотографию"
    }

    fun uploadUserImage(imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            _error.value = null
            try {
                val oldImageUrl = imageUrl.value.orEmpty()

                val response = usersRepository.uploadUserImage(getCurrentUserId(), imagePart)

                if (response.isSuccessful || response.code() == 405) {
                    retryUntilImageChanges(oldImageUrl)
                } else {
                    _error.value = "Ошибка загрузки фото: ${response.code()} ${response.message()}"
                }

            } catch (e: Exception) {
                _error.value = "Ошибка загрузки фото: ${e.localizedMessage}"
            }
        }
    }

    private suspend fun getCurrentUserId(): UUID {
        return withContext(Dispatchers.IO) {
            usersRepository.getCurrentUserId()
        }
    }

    private suspend fun getCurrentUsername(): String {
        return withContext(Dispatchers.IO) {
            usersRepository.getCurrentUserName()
        }
    }

    private fun getNumSubscribers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _subscribers.value =
                    blogSubscriberRepository.subscribers(getCurrentUserId()).toString()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getNumSubscriptions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _subscriptions.value =
                    blogSubscriberRepository.subscriptions(getCurrentUserId()).toString()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                _blogRecords.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val token = AuthManager.getAccessToken(getApplication())
                if (token != null) {
                    withContext(Dispatchers.IO) {
                        authRepository.logout("Bearer $token")
                    }
                }

                _userName.value = null
                _imageUrl.value = null
                _blogRecords.value = emptyList()
                _subscribers.value = ""
                _subscriptions.value = ""

                AuthManager.clear(getApplication())
                _logoutState.value = Result.success(Unit)
            } catch (e: Exception) {
                _logoutState.value = Result.failure(e)
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    usersRepository.deleteUser(getCurrentUserId().toString())
                }
                _deleteState.value = Result.success(Unit)
                logout()
            } catch (e: Exception) {
                _deleteState.value = Result.failure(e)
            }
        }
    }

    fun refresh() {
        loadHairType()
        loadBlogRecords()
        loadProfile()
        getNumSubscribers()
        getNumSubscriptions()
    }
}