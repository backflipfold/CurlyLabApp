package aps.backflip.curlylab.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Instant
import java.util.UUID

class TokenService(
    private val secret: String,
    private val validityInDays: Long = 30
) {
    fun generateAccessToken(userId: UUID, username: String): String {
        return JWT.create()
            .withSubject(userId.toString())
            .withClaim("username", username)
            .withExpiresAt(Instant.now().plusSeconds(validityInDays * 24 * 3600))
            .sign(Algorithm.HMAC256(secret))
    }

    fun validateToken(token: String): Boolean {
        return try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}