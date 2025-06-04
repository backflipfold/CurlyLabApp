package aps.backflip.curlylab.controllers

import aps.backflip.curlylab.models.HairTypeRequest
import aps.backflip.curlylab.models.HairTypeResponse
import aps.backflip.curlylab.repositories.HairTypeRepository
import java.util.UUID

class HairTypesController(
    private val repository: HairTypeRepository = HairTypeRepository()
) {
    suspend fun getHairType(userId: UUID): HairTypeResponse? {
        return repository.getHairType(userId)
    }

    suspend fun updateHairType(userId: UUID, request: HairTypeRequest) {
        repository.updateHairType(userId, request)
    }

    suspend fun deleteHairType(userId: UUID) {
        repository.deleteHairType(userId)
    }
}