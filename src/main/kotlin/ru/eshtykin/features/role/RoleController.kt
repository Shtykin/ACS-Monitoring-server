package ru.eshtykin.features.role

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.constatns.publ.Constants
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.users.Users


class RoleController(private val call: ApplicationCall) {

    suspend fun setRoleUser() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token.isNullOrEmpty() || !TokenCheck.isTokenAdmin(token = token)) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return
        }

        val roleReceiveRemote = call.receive<RoleReceiveRemote>()
        if (roleReceiveRemote.role != Constants.roleDevice && roleReceiveRemote.role != Constants.roleExplorer){
            call.respond(HttpStatusCode.Unauthorized, "Invalid role")
            return
        }
        val user = Users.fetch(userLogin = roleReceiveRemote.login)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return
        }

        val newUser = user.copy(role = roleReceiveRemote.role)
        if (Users.update(newUser)) {
            call.respond(RoleResponseRemote(login = newUser.login, role = newUser.role ?: ""))
        } else {
            call.respond(HttpStatusCode.Conflict, "Failed to change role")
        }
    }
}