package aps.backflip.curlylab.controllers

import aps.backflip.curlylab.config.DriveConfig
import aps.backflip.curlylab.models.UserRequest
import aps.backflip.curlylab.models.UserResponse
import aps.backflip.curlylab.repositories.AuthRepository
import aps.backflip.curlylab.repositories.HairTypeRepository
import aps.backflip.curlylab.repositories.UserRepository
import aps.backflip.curlylab.services.GoogleDriveService
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory
import java.util.UUID

class UsersController(
    private val hairTypesRepository: HairTypeRepository = HairTypeRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
    private val repository: UserRepository = UserRepository(hairTypesRepository, authRepository)
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun createUser(request: UserRequest): UUID {
        validateCreateRequest(request)
        return repository.createUser(
            username = request.username!!,
            language = request.language ?: "ru"
        )
    }

    suspend fun getUser(userId: UUID): UserResponse? {
        return repository.getUser(userId)
    }

    suspend fun getAllUsers(): List<UserResponse> {
        logger.info("Fetching all users...")
        return repository.getAllUsers()
    }

    suspend fun updateUser(userId: UUID, request: UserRequest) {
        repository.updateUser(userId, request)
    }

    suspend fun uploadUserImage(userId: UUID, fileBytes: ByteArray, fileName: String): String {
        val user = repository.getUser(userId)
            ?: throw NotFoundException("User not found")

        val driveService = DriveConfig.createDriveService()
        val driveUploader = GoogleDriveService(driveService)

        try {
            user.imageFileId?.let { fileId ->
                try {
                    driveUploader.deleteFile(fileId)
                } catch (e: GoogleJsonResponseException) {
                    if (e.statusCode != 404) {
                        throw e
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error deleting old image: ${e.message}")
        }

        val (imageUrl, fileId) = driveUploader.uploadUserImage(
            userId,
            fileBytes.inputStream(),
            fileName
        )
        repository.updateUserImage(userId, imageUrl, fileId)
        return imageUrl
    }

    suspend fun deleteUser(userId: UUID) {
        repository.deleteUser(userId)
    }

    private fun validateCreateRequest(request: UserRequest) {
        when {
            request.username.isNullOrEmpty() ->
                throw IllegalArgumentException("Username is required")

            request.language?.length != 2 ->
                throw IllegalArgumentException("Language must be 2-letter code")
        }
    }
}