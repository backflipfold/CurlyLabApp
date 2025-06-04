package aps.backflip.curlylab.data.repository.profile

import android.content.Context
import aps.backflip.curlylab.data.local.preferences.AuthManager
import aps.backflip.curlylab.data.remote.api.ApiService
import aps.backflip.curlylab.data.remote.model.request.profile.UserRequest
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import okhttp3.MultipartBody
import retrofit2.Response
import okhttp3.ResponseBody
import java.util.UUID
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) :
    UsersRepository {
    override suspend fun getAllUsers() = apiService.getAllUsers()
    override suspend fun getUser(id: String) = apiService.getUser(id)
    override suspend fun createUser(request: UserRequest): String =
        apiService.createUser(request)["id"] ?: throw IllegalStateException("No ID returned")

    override suspend fun updateUser(id: String, request: UserRequest) {
        apiService.updateUser(id, request)
    }


    override suspend fun uploadUserImage(
        userId: UUID,
        imagePart: MultipartBody.Part
    ): Response<ResponseBody> {
        return apiService.uploadUserImage(userId, imagePart)
    }
    override suspend fun deleteUser(id: String) {
        apiService.deleteUser(id)
    }

    override suspend fun getCurrentUserId(): UUID {
        val con = context
        val id = AuthManager.getUserId(con)
            ?: throw IllegalStateException("User ID not available")
        return UUID.fromString(id)
    }

    override suspend fun getCurrentUserName(): String {
        return AuthManager.getUsername(context)
            ?: throw IllegalStateException("User name not available")
    }
}