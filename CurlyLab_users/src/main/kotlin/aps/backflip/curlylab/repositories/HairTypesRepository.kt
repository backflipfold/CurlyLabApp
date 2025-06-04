package aps.backflip.curlylab.repositories

import aps.backflip.curlylab.models.HairTypeRequest
import aps.backflip.curlylab.models.HairTypeResponse
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class HairTypeRepository {
    object UserHairTypes : Table("user_hairtypes") {
        val userId =
            uuid("user_id").references(UserRepository.Users.id, onDelete = ReferenceOption.CASCADE)
        val porosity = varchar("porosity", 50)
        val isColored = bool("is_colored")
        val thickness = varchar("thickness", 50)
        override val primaryKey = PrimaryKey(userId)
    }

    suspend fun getHairType(userId: UUID): HairTypeResponse? = transaction {
        UserHairTypes
            .select { UserHairTypes.userId eq userId }
            .singleOrNull()
            ?.let {
                HairTypeResponse(
                    porosity = it[UserHairTypes.porosity],
                    isColored = it[UserHairTypes.isColored],
                    thickness = it[UserHairTypes.thickness]
                )
            }
    }

    fun createDefaultHairType(userId: UUID) = transaction {
        UserHairTypes.insert {
            it[UserHairTypes.userId] = userId
            it[porosity] = "Не выбрано"
            it[isColored] = false
            it[thickness] = "Не выбрано"
        }
    }

    suspend fun updateHairType(userId: UUID, request: HairTypeRequest) = transaction {
        val current = UserHairTypes
            .select { UserHairTypes.userId eq userId }
            .singleOrNull() ?: error("Тип волос не найден для пользователя $userId")

        val newPorosity = request.porosity ?: current[UserHairTypes.porosity]
        val newIsColored = request.isColored ?: current[UserHairTypes.isColored]
        val newThickness = request.thickness ?: current[UserHairTypes.thickness]

        UserHairTypes.update({ UserHairTypes.userId eq userId }) {
            it[porosity] = newPorosity
            it[isColored] = newIsColored
            it[thickness] = newThickness
        }
    }

    fun deleteHairType(userId: UUID) = transaction {
        UserHairTypes.deleteWhere { UserHairTypes.userId eq userId }
    }
}