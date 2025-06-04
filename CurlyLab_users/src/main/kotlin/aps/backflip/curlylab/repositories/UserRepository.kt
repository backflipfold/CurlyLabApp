package aps.backflip.curlylab.repositories

import aps.backflip.curlylab.models.UserRequest
import aps.backflip.curlylab.models.UserResponse
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class UserRepository(
    private val hairTypeRepository: HairTypeRepository,
    private val authRepository: AuthRepository
) {
    object Users : Table("users") {
        val id = uuid("id")
        val username = varchar("username", 100)
        val language = varchar("language", 2).default("ru")
        val createdAt = timestamp("created_at").clientDefault { java.time.Instant.now() }
        val imageUrl = varchar("image_url", 512).nullable()
        val imageFileId = varchar("image_file_id", 128).nullable()
        override val primaryKey = PrimaryKey(id)
    }

    suspend fun createUser(username: String, language: String): UUID = transaction {
        val userId = UUID.randomUUID()

        Users.insert {
            it[id] = userId
            it[Users.username] = username
            it[Users.language] = language
        }

        hairTypeRepository.createDefaultHairType(userId)

        userId
    }

    suspend fun getUser(userId: UUID): UserResponse? = transaction {
        Users.select { Users.id eq userId }
            .singleOrNull()
            ?.let {
                UserResponse(
                    id = it[Users.id],
                    username = it[Users.username],
                    language = it[Users.language],
                    createdAt = convertToKotlinInstant(it[Users.createdAt]),
                    imageUrl = it[Users.imageUrl],
                    imageFileId = it[Users.imageFileId]
                )
            }
    }

    suspend fun getAllUsers(): List<UserResponse> = transaction {
        Users.selectAll().map {
            UserResponse(
                id = it[Users.id],
                username = it[Users.username],
                language = it[Users.language],
                createdAt = convertToKotlinInstant(it[Users.createdAt]),
                imageUrl = it[Users.imageUrl]
            )
        }
    }

    suspend fun updateUser(userId: UUID, request: UserRequest) = transaction {
        Users.update({ Users.id eq userId }) {
            request.username?.let { username -> it[Users.username] = username }
            request.language?.let { language -> it[Users.language] = language }
            request.imageUrl?.let { url -> it[Users.imageUrl] = url }
        }
    }

    suspend fun updateUserImage(userId: UUID, imageUrl: String, fileId: String) = transaction {
        Users.update({ Users.id eq userId }) {
            it[Users.imageUrl] = imageUrl
            it[Users.imageFileId] = fileId
        }
    }

    suspend fun deleteUser(userId: UUID) = transaction {
        authRepository.deleteRefreshTokens(userId)
        authRepository.deleteAuth(userId)
        hairTypeRepository.deleteHairType(userId)

        Users.deleteWhere { Users.id eq userId }
    }

    private fun convertToKotlinInstant(javaInstant: java.time.Instant): Instant {
        return Instant.parse(javaInstant.toString())
    }
}