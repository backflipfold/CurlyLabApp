package aps.backflip.curlylab.repositories

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID

class AuthRepository {
    object UserAuth : Table("user_auth") {
        val userId =
            uuid("user_id").references(UserRepository.Users.id, onDelete = ReferenceOption.CASCADE)
        val email = varchar("email", 255).uniqueIndex()
        val passwordHash = varchar("password_hash", 255)
        val salt = varchar("salt", 255)
        val isVerified = bool("is_verified").default(false)
        val lastLogin = timestamp("last_login").nullable()
        val createdAt = timestamp("created_at").default(Instant.now())
        override val primaryKey = PrimaryKey(userId)
    }

    object UserRefreshTokens : Table("user_refresh_tokens") {
        val id = uuid("id").clientDefault { UUID.randomUUID() }
        val userId =
            uuid("user_id").references(UserRepository.Users.id, onDelete = ReferenceOption.CASCADE)
        val token = varchar("token", 255)
        val expiresAt = timestamp("expires_at")
        val createdAt = timestamp("created_at").clientDefault { Instant.now() }
        val revoked = bool("revoked").default(false)

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun findByEmail(email: String): AuthEntity? = transaction {
        UserAuth
            .select { UserAuth.email eq email }
            .singleOrNull()
            ?.let {
                AuthEntity(
                    userId = it[UserAuth.userId],
                    email = it[UserAuth.email],
                    passwordHash = it[UserAuth.passwordHash],
                    salt = it[UserAuth.salt],
                    isVerified = it[UserAuth.isVerified]
                )
            }
    }

    suspend fun createAuth(
        userId: UUID,
        email: String,
        passwordHash: String,
        salt: String
    ) = transaction {
        UserAuth.insert {
            it[UserAuth.userId] = userId
            it[UserAuth.email] = email
            it[UserAuth.passwordHash] = passwordHash
            it[UserAuth.salt] = salt
        }
    }

    suspend fun saveRefreshToken(userId: UUID, token: String, expiresAt: Instant) = transaction {
        UserRefreshTokens.insert {
            it[UserRefreshTokens.userId] = userId
            it[UserRefreshTokens.token] = token
            it[UserRefreshTokens.expiresAt] = expiresAt
        }
    }

    suspend fun updateLastLogin(userId: UUID) = transaction {
        UserAuth.update({ UserAuth.userId eq userId }) {
            it[lastLogin] = Instant.now()
        }
    }

    suspend fun validateRefreshToken(token: String): UUID? = transaction {
        UserRefreshTokens
            .select {
                UserRefreshTokens.token eq token and
                        (UserRefreshTokens.expiresAt greaterEq Instant.now()) and
                        (UserRefreshTokens.revoked eq false)
            }
            .singleOrNull()
            ?.get(UserRefreshTokens.userId)
    }

    suspend fun revokeToken(token: String) = transaction {
        UserRefreshTokens.update({ UserRefreshTokens.token eq token }) {
            it[revoked] = true
        }
    }

    fun deleteAuth(userId: UUID) = transaction {
        UserAuth.deleteWhere { UserAuth.userId eq userId }
    }

    fun deleteRefreshTokens(userId: UUID) = transaction {
        UserRefreshTokens.deleteWhere { UserRefreshTokens.userId eq userId }
    }
}

data class AuthEntity(
    val userId: UUID,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val isVerified: Boolean
)