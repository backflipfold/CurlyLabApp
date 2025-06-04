package aps.backflip.curlylab.config

import aps.backflip.curlylab.repositories.AuthRepository
import aps.backflip.curlylab.repositories.HairTypeRepository
import aps.backflip.curlylab.repositories.UserRepository
import aps.backflip.curlylab.services.TokenService
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.config.HoconApplicationConfig

object Dependencies {
    lateinit var hairTypeRepository: HairTypeRepository
    lateinit var userRepository: UserRepository
    lateinit var authRepository: AuthRepository
    lateinit var tokenService: TokenService

    fun Application.init(application: Application) {
        val config = HoconApplicationConfig(ConfigFactory.load())

        hairTypeRepository = HairTypeRepository()
        authRepository = AuthRepository()
        userRepository = UserRepository(hairTypeRepository, authRepository)

        tokenService = TokenService(
            secret = config.property("jwt.secret").getString(),
            validityInDays = config.property("jwt.validityDays").getString().toLong()
        )
    }
}