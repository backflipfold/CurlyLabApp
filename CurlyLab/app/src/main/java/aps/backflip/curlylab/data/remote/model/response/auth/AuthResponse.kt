package aps.backflip.curlylab.data.remote.model.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String
)