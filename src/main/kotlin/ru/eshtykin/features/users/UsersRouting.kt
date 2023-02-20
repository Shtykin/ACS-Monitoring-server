package ru.eshtykin.features.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.users.Users

fun Application.configureUsersRouting() {
    routing {
        post("/admin/user") {
            val usersController = UsersController(call)
            usersController.getUser()
        }
        post("/admin/users") {
            val usersController = UsersController(call)
            usersController.getUsers()
        }
        post("/admin/owner") {
            val usersController = UsersController(call)
            usersController.setOwner()
        }

    }
}