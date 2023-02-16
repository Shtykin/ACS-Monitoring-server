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



//        post("/registers/add") {
//            val registerController = RegisterController(call)
//            registerController.setRegister()
//        }
//        post("/registers/fetch") {
//            val registerController = RegisterController(call)
//            registerController.getRegister()
//        }
    }
}