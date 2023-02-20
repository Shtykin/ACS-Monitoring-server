package ru.eshtykin.features.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.owners.OwnerDTO
import ru.eshtykin.database.owners.Owners
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.users.Users
import ru.eshtykin.features.login.LoginResponseRemote
import java.util.*

class UsersController(private val call: ApplicationCall) {

    suspend fun getUser() {
        if (!checkAdmin()) return
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
                role = user.role,
                owner = Owners.fetchByLogin(user.login)?.map { it.owner }
            )
        )
    }

    suspend fun getUsers() {
        if (!checkAdmin()) return
        val users = Users.fetchAll()?.map { it ->
            UserResponseRemote(
                login = it.login,
                email = it.email,
                role = it.role,
                owner = Owners.fetchByLogin(it.login)?.map { it.owner }
            )
        }
        if (users.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Users not found")
            return
        }
        call.respond(users)
    }

    suspend fun setOwner() {
        if (!checkAdmin()) return
        val ownerReceiveRemote = call.receive<OwnerReceiveRemote>()
        val user = Users.fetch(userLogin = ownerReceiveRemote.login)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return
        }
        val ownerDTO = OwnerDTO(
            id = UUID.randomUUID().toString(),
            login = ownerReceiveRemote.login,
            owner = ownerReceiveRemote.owner
        )
        Owners.insert(ownerDTO)
        call.respond(OwnerResponseRemote(login =ownerDTO.login, owner = ownerDTO.owner))
    }

    private suspend fun checkAdmin(): Boolean {
        val token = call.request.headers["Baerer-Autorization"]
        return if (!token.isNullOrEmpty() && TokenCheck.isTokenAdmin(token = token)) {
            true
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            false
        }
    }

}