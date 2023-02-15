package ru.eshtykin

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import ru.eshtykin.features.login.configureLoginRouting
import ru.eshtykin.features.registration.configureRegistrationRouting
import ru.eshtykin.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureLoginRouting()
    configureRegistrationRouting()
}
