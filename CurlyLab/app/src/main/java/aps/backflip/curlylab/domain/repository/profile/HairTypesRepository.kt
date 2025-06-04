package aps.backflip.curlylab.domain.repository.profile

import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import aps.backflip.curlylab.data.remote.model.response.profile.HairTypeResponse

interface HairTypesRepository {
    //suspend fun createHairType(userId: String, request: HairTypeRequest): String
    suspend fun getAllHairTypes(): List<HairTypeResponse>
    suspend fun getHairType(id: String): HairTypeResponse
    suspend fun updateHairType(userId: String, request: HairTypeRequest)
    suspend fun deleteHairType(userId: String)
}