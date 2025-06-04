package aps.backflip.curlylab.config

import aps.backflip.curlylab.repositories.AuthRepository
import aps.backflip.curlylab.repositories.HairTypeRepository
import aps.backflip.curlylab.repositories.UserRepository
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {
    fun Application.init(application: Application) {
        val config = HoconApplicationConfig(ConfigFactory.load())

        Database.connect(
            url = config.property("database.url").getString(),
            driver = config.property("database.driver").getString(),
            user = config.property("database.user").getString(),
            password = config.property("database.password").getString()
        )

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(
                UserRepository.Users,
                HairTypeRepository.UserHairTypes,
                AuthRepository.UserAuth,
                AuthRepository.UserRefreshTokens
            )
        }
    }
}