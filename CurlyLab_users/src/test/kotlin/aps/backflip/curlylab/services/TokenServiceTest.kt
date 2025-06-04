package aps.backflip.curlylab.services

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class TokenServiceTest {

    private val secret = "supersecret"
    private val service = TokenService(secret)

    @Test
    fun shouldGenerateValidToken() {
        val userId = UUID.randomUUID()
        val username = "testuser"
        val token = service.generateAccessToken(userId, username)

        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertTrue(service.validateToken(token))
    }

    @Test
    fun shouldReturnFalseForInvalidToken() {
        val invalidToken = "this.is.not.valid"
        assertFalse(service.validateToken(invalidToken))
    }
}
