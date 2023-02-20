package ru.eshtykin.features.registration

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegistrationRouting() {
    routing {
        post("/registration") {
            val registerController = RegistrationController(call)
            registerController.registrationNewUser()
        }
    }
}