package ru.eshtykin.features.registration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.tokens.TokenDTO
import ru.eshtykin.database.tokens.Tokens
import ru.eshtykin.database.users.UserDTO
import ru.eshtykin.database.users.Users
import ru.eshtykin.utils.isValidEmail
import java.util.*

class RegistrationController(private val call: ApplicationCall) {

    suspend fun registrationNewUser() {
        val registrationReceiveRemote = call.receive<RegistrationReceiveRemote>()
        if (!registrationReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        } else {
            val userDTO = Users.fetch(registrationReceiveRemote.login)
            if (userDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User already exist")
            } else {
                val token = UUID.randomUUID().toString()
                    Users.insert(
                        UserDTO(
                            login = registrationReceiveRemote.login,
                            password = registrationReceiveRemote.password,
                            email = registrationReceiveRemote.email
                        )
                    )
                Tokens.insert(
                    TokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = registrationReceiveRemote.login,
                        token = token
                    )
                )
                call.respond(RegistrationResponseRemote(token = token))
            }
        }
    }
}