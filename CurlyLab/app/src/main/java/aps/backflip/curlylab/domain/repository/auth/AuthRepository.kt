package aps.backflip.curlylab.domain.repository.auth

import aps.backflip.curlylab.data.remote.model.request.auth.LoginRequest
import aps.backflip.curlylab.data.remote.model.request.auth.RegisterRequest
import aps.backflip.curlylab.data.remote.model.response.auth.AuthResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: RegisterRequest): AuthResponse
    suspend fun refreshToken(refreshToken: String): AuthResponse
    suspend fun logout(token: String)
}