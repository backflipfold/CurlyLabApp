package aps.backflip.curlylab.utils

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object SecurityUtils {
    private const val SALT_LENGTH = 32
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256

    fun generateSalt(): String {
        val secureRandom = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        secureRandom.nextBytes(salt)
        return salt.toHex()
    }

    fun hashPassword(password: String, salt: String): String {
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt.fromHex(),
            ITERATIONS,
            KEY_LENGTH
        )
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return hash.toHex()
    }

    fun verifyPassword(password: String, salt: String, storedHash: String): Boolean {
        return hashPassword(password, salt) == storedHash
    }

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
    private fun String.fromHex(): ByteArray = chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}