package aps.backflip.curlylab.presentation.blogrecords.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindRecordsViewModel @Inject constructor(
    private val repository: BlogRecordRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _blogRecords = MutableStateFlow<List<BlogRecord>>(emptyList())
    val blogRecords: StateFlow<List<BlogRecord>> = _blogRecords.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var searchQuery = ""


    init {
        findPosts(searchQuery)
    }

    fun findPosts(word: String) {
        searchQuery = word
        if (word == ""){

        } else {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    _blogRecords.value = repository.findPosts(word).map { blogRecordResponse ->
                        BlogRecord(
                            content = blogRecordResponse.content,
                            recordId = blogRecordResponse.recordId,
                            userId = blogRecordResponse.userId,
                            tags = blogRecordResponse.tags,
                            createdAt = blogRecordResponse.createdAt,
                            userName = usersRepository.getUser(blogRecordResponse.userId.toString()).username
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
    }

    fun refresh() {
        findPosts(searchQuery)
    }
}