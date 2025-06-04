package aps.backflip.curlylab.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val language: String = "ru"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val userId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
