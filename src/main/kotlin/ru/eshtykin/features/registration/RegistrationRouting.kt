package ru.eshtykin.features.registration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.eshtykin.cache.InMemoryCache
import ru.eshtykin.cache.TokenCache
import ru.eshtykin.utils.isValidEmail
import java.util.*

fun Application.configureRegistrationRouting() {
    routing {
        post("/registration") {
            val receive = call.receive<RegistrationReceiveRemote>()
            if (!receive.email.isValidEmail()){
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            } else if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                call.respond(HttpStatusCode.Conflict, "User already exist")
            } else {
                val token = UUID.randomUUID().toString()
                InMemoryCache.userList.add(receive)
                InMemoryCache.tokenList.add(TokenCache(login = receive.login, token = token))
                call.respond(RegistrationResponseRemote(token = token))
            }
        }
    }
}