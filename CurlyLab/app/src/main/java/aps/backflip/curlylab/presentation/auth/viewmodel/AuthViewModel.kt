package aps.backflip.curlylab.presentation.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.local.preferences.AuthManager
import aps.backflip.curlylab.data.remote.model.request.auth.LoginRequest
import aps.backflip.curlylab.data.remote.model.request.auth.RegisterRequest
import aps.backflip.curlylab.data.remote.model.response.auth.AuthResponse
import aps.backflip.curlylab.domain.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    application: Application
) : AndroidViewModel(application) {

    val loginState = MutableStateFlow<Result<AuthResponse>?>(null)
    val registerState = MutableStateFlow<Result<AuthResponse>?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.login(LoginRequest(email, password))
                }
                AuthManager.saveAuthData(getApplication(), response)
                loginState.value = Result.success(response)
            } catch (e: Exception) {
                loginState.value = Result.failure(e)
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.register(RegisterRequest(email, password, username))
                }
                AuthManager.saveAuthData(getApplication(), response)
                registerState.value = Result.success(response)
            } catch (e: Exception) {
                registerState.value = Result.failure(e)
            }
        }
    }
}