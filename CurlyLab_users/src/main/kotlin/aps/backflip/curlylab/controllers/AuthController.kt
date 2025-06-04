package aps.backflip.curlylab.controllers

import aps.backflip.curlylab.models.AuthResponse
import aps.backflip.curlylab.models.LoginRequest
import aps.backflip.curlylab.models.RefreshTokenRequest
import aps.backflip.curlylab.models.RegisterRequest
import aps.backflip.curlylab.repositories.AuthRepository
import aps.backflip.curlylab.repositories.UserRepository
import aps.backflip.curlylab.services.TokenService
import aps.backflip.curlylab.utils.SecurityUtils
import java.time.Instant
import java.util.UUID

class AuthController(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenService: TokenService
) {
    suspend fun register(request: RegisterRequest): AuthResponse {
        if (authRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already registered")
        }

        val salt = SecurityUtils.generateSalt()
        val passwordHash = SecurityUtils.hashPassword(request.password, salt)

        val userId = userRepository.createUser(
            username = request.username,
            language = request.language
        )

        authRepository.createAuth(
            userId = userId,
            email = request.email,
            passwordHash = passwordHash,
            salt = salt
        )

        return generateTokens(userId)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val auth = authRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("User not found")

        if (!SecurityUtils.verifyPassword(request.password, auth.salt, auth.passwordHash)) {
            throw IllegalArgumentException("Invalid password")
        }

        authRepository.updateLastLogin(auth.userId)
        return generateTokens(auth.userId)
    }

    suspend fun refresh(request: RefreshTokenRequest): AuthResponse {
        val userId = authRepository.validateRefreshToken(request.refreshToken)
            ?: throw IllegalArgumentException("Invalid refresh token")

        return generateTokens(userId)
    }

    suspend fun invalidateToken(token: String) {
        authRepository.revokeToken(token)
    }

    private suspend fun generateTokens(userId: UUID): AuthResponse {
        val user = userRepository.getUser(userId) ?: throw IllegalStateException("User not found")
        val accessToken = tokenService.generateAccessToken(userId, user.username)
        val refreshToken = UUID.randomUUID().toString()

        authRepository.saveRefreshToken(
            userId = userId,
            token = refreshToken,
            expiresAt = Instant.now().plusSeconds(30 * 24 * 3600)
        )

        return AuthResponse(
            userId = userId.toString(),
            username = user.username,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}