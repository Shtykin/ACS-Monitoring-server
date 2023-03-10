package ru.eshtykin.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.tokens.TokenDTO
import ru.eshtykin.database.tokens.Tokens
import ru.eshtykin.database.tokens.admin.AdminTokenDTO
import ru.eshtykin.database.tokens.admin.AdminTokens
import ru.eshtykin.database.users.Users
import ru.eshtykin.constatns.private.Constants
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val loginReceiveRemote = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetch(loginReceiveRemote.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == loginReceiveRemote.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = loginReceiveRemote.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }

    suspend fun performAdminLogin() {
        val loginReceiveRemote = call.receive<LoginAdminReceiveRemote>()

        if (loginReceiveRemote.login != Constants.adminLogin){
            call.respond(HttpStatusCode.BadRequest, "Admin not found")
            return
        }
        if (loginReceiveRemote.password != Constants.adminPass) {
            call.respond(HttpStatusCode.BadRequest, "Invalid password")
        }

        val token = UUID.randomUUID().toString()
        AdminTokens.insert(AdminTokenDTO(
            id = UUID.randomUUID().toString(),
            login = loginReceiveRemote.login,
            token = token
        ))
        call.respond(LoginResponseRemote(token = token))
    }
}