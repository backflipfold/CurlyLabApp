package aps.backflip.curlylab.config

import aps.backflip.curlylab.controllers.AuthController
import aps.backflip.curlylab.controllers.HairTypesController
import aps.backflip.curlylab.controllers.UsersController
import aps.backflip.curlylab.routes.configureRoutes
import com.benasher44.uuid.Uuid
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

object UuidSerializer : KSerializer<Uuid> {
    override val descriptor = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uuid {
        return Uuid.fromString(decoder.decodeString())
    }
}

private fun Application.configureLogging() {
    install(CallLogging) {
        level = org.slf4j.event.Level.DEBUG
    }
}

object ApplicationConfig {
    fun Application.configure() {
        with(DatabaseConfig) {
            init(this@configure)
        }

        with(Dependencies) {
            init(this@configure)
        }

        configureLogging()
        configureErrorHandling()
        configureSerialization()

        routing {
            val authController = AuthController(
                Dependencies.userRepository,
                Dependencies.authRepository,
                Dependencies.tokenService
            )
            configureRoutes(
                authController = authController,
                usersController = UsersController(
                    Dependencies.hairTypeRepository,
                    Dependencies.authRepository,
                    Dependencies.userRepository
                ),
                profilesController = HairTypesController()
            )
        }
    }

    private fun Application.configureSerialization() {
        val json = Json {
            serializersModule = SerializersModule {
                contextual(Uuid::class, UuidSerializer)
            }
            prettyPrint = true
        }

        install(ContentNegotiation) {
            json(json)
        }
    }
}