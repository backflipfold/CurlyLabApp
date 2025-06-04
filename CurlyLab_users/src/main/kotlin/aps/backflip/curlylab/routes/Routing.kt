package aps.backflip.curlylab.routes

import aps.backflip.curlylab.controllers.AuthController
import aps.backflip.curlylab.controllers.HairTypesController
import aps.backflip.curlylab.controllers.UsersController
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Routing.configureRoutes(
    authController: AuthController,
    usersController: UsersController,
    profilesController: HairTypesController
) {
    route("") {
        authRoutes(authController)
        usersRoutes(usersController)
        hairTypesRoutes(profilesController)
    }
}