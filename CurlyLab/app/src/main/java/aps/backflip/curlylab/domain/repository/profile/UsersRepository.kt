package aps.backflip.curlylab.domain.repository.profile

import aps.backflip.curlylab.data.remote.model.request.profile.UserRequest
import aps.backflip.curlylab.data.remote.model.response.profile.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.UUID

interface UsersRepository {
    suspend fun createUser(request: UserRequest): String
    suspend fun getAllUsers(): List<UserResponse>
    suspend fun getUser(id: String): UserResponse
    suspend fun updateUser(id: String, request: UserRequest)
    suspend fun uploadUserImage(userId: UUID, imagePart: MultipartBody.Part): Response<ResponseBody>
    suspend fun deleteUser(id: String)
    suspend fun getCurrentUserId(): UUID
    suspend fun getCurrentUserName(): String
}