package ru.eshtykin.features.registers

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        post("/explorer/register/read") {
            val registerController = RegisterController(call)
            registerController.readRegisterByExplorer()
        }
        post("/explorer/registers/read") {
            val registerController = RegisterController(call)
            registerController.readRegistersByExplorer()
        }
        post("/explorer/register/set") {
            val registerController = RegisterController(call)
            registerController.setRegisterByExplorer()
        }
        post("/explorer/registers/set") {
            val registerController = RegisterController(call)
            registerController.setRegistersByExplorer()
        }
        post("/device/register/set") {
            val registerController = RegisterController(call)
            registerController.setRegisterByDevice()
        }
        post("/device/registers/set") {
            val registerController = RegisterController(call)
            registerController.setRegistersByDevice()
        }
    }
}