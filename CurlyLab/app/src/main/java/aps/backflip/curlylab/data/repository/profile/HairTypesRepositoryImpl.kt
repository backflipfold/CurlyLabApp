package aps.backflip.curlylab.data.repository.profile

import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import aps.backflip.curlylab.data.remote.model.response.profile.HairTypeResponse
import aps.backflip.curlylab.data.remote.api.ApiService
import javax.inject.Inject

class HairTypesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HairTypesRepository {

//    override suspend fun createHairType(userId: String, request: HairTypeRequest): String {
//        val response = apiService.createHairType(userId, request)
//        return response["id"] ?: throw IllegalStateException("No ID returned from server")
//    }

    override suspend fun getAllHairTypes(): List<HairTypeResponse> {
        return apiService.getAllHairTypes()
    }

    override suspend fun getHairType(id: String): HairTypeResponse {
        return apiService.getHairType(id)
    }

    override suspend fun updateHairType(userId: String, request: HairTypeRequest) {
        apiService.updateHairType(userId, request)
    }

    override suspend fun deleteHairType(userId: String) {
        apiService.deleteHairType(userId)
    }
}