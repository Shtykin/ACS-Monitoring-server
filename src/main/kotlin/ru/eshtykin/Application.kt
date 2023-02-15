package ru.eshtykin

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.eshtykin.features.login.configureLoginRouting
import ru.eshtykin.features.registration.configureRegistrationRouting
import ru.eshtykin.plugins.*
import ru.eshtykin.private.Constants

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/acs_monitoring",
        driver = "org.postgresql.Driver",
        user =  Constants.userDb,
        password = Constants.passwordDb
    )
//    println("db: ${db.version}")
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureLoginRouting()
    configureRegistrationRouting()
}
