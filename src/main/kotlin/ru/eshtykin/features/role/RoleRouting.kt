package ru.eshtykin.features.role

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.users.Users

fun Application.configureRoleRouting() {
    routing {
        post("/admin/role") {
            val roleController = RoleController(call)
            roleController.setRoleUser()
        }
    }
}