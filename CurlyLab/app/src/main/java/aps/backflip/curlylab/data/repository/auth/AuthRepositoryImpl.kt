package aps.backflip.curlylab.data.repository.auth

import aps.backflip.curlylab.domain.repository.auth.AuthRepository
import aps.backflip.curlylab.data.remote.model.request.auth.RefreshTokenRequest
import aps.backflip.curlylab.data.remote.model.request.auth.LoginRequest
import aps.backflip.curlylab.data.remote.model.request.auth.RegisterRequest
import aps.backflip.curlylab.data.remote.model.response.auth.AuthResponse
import aps.backflip.curlylab.data.remote.api.ApiService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): AuthResponse {
        return apiService.login(request)
    }

    override suspend fun register(request: RegisterRequest): AuthResponse {
        return apiService.register(request)
    }

    override suspend fun refreshToken(refreshToken: String): AuthResponse {
        return apiService.refresh(RefreshTokenRequest(refreshToken))
    }

    override suspend fun logout(token: String) {
        apiService.logout("Bearer $token")
    }
}