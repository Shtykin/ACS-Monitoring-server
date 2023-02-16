package ru.eshtykin.features.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.users.Users

class UsersController(private val call: ApplicationCall) {

    suspend fun getUser() {
        val token = call.request.headers["Baerer-Autorization"]
        if (!TokenCheck.isTokenAdmin(token = token)) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return
        }

        val userReceiveRemote = call.receive<UserReceiveRemote>()
        val user = Users.fetch(userLogin = userReceiveRemote.login)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return
        }
        call.respond(
            UserResponseRemote(
                login = user.login,
                email = user.email,
                role = user.role
            )
        )
    }

    suspend fun getUsers() {
        val token = call.request.headers["Baerer-Autorization"]
        if (!TokenCheck.isTokenAdmin(token = token)) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return
        }

        val users = Users.fetchAll()?.map { UserResponseRemote(
            login = it.login,
            email = it.email,
            role = it.role
        ) }
        if (users.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Users not found")
            return
        }
        call.respond(users)
    }

}