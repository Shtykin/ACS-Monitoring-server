package ru.eshtykin.features.registers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.registers.RegisterDTO
import ru.eshtykin.database.registers.Registers
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.tokens.TokenDTO
import ru.eshtykin.database.tokens.Tokens
import ru.eshtykin.database.users.UserDTO
import ru.eshtykin.database.users.Users
import ru.eshtykin.features.login.LoginReceiveRemote
import ru.eshtykin.features.login.LoginResponseRemote
import ru.eshtykin.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun readRegisterByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (!TokenCheck.isTokenValid(token.orEmpty())) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return
        }
        val registerReceiveRemote = call.receive<RegisterByExplorerReceiveRemote>()

        val register = Registers
            .fetchForOwner(registerOwner = registerReceiveRemote.owner)
            ?.filter { it.adress == registerReceiveRemote.adress }?.first()

        if (register == null) {
            call.respond(HttpStatusCode.BadRequest, "Register not found")
            return
        }
        call.respond(register.mapToRegisterResponseRemote())
    }

    suspend fun readRegistersByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (!TokenCheck.isTokenValid(token.orEmpty())) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return
        }
        val registersReceiveRemote = call.receive<RegistersByExplorerReceiveRemote>()

        val registers = Registers
            .fetchForOwner(registerOwner = registersReceiveRemote.owner)

        if (registers.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Registers not found")
            return
        }
        call.respond(registers.map { it.mapToRegisterResponseRemote() })
    }


//    suspend fun setRegister() {
//        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
//        val token = call.request.headers["Baerer-Autorization"]
//        if (!TokenCheck.isTokenValid(token.orEmpty())) {
//            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
//        } else {
//            val oldRegisterDTO = Registers.fetchForAdress(registerReceiveRemote.adress)
//            val newRegisterDTO = RegisterDTO(
//                adress = registerReceiveRemote.adress,
//                name = registerReceiveRemote.name,
//                value = registerReceiveRemote.value,
//                unit = registerReceiveRemote.unit,
//                timestamp = registerReceiveRemote.timestamp ?: System.currentTimeMillis(),
//                owner = registerReceiveRemote.owner
//            )
//            if (oldRegisterDTO == null) {
//                Registers.insert(newRegisterDTO)
//                call.respond(HttpStatusCode.Created, "Register created")
//            } else {
//                Registers.update(newRegisterDTO)
//                call.respond(HttpStatusCode.OK, "Register updated")
//            }
//        }
//    }
//
//    suspend fun getRegister() {
//        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
//        val token = call.request.headers["Baerer-Autorization"]
//
//        if (!TokenCheck.isTokenValid(token.orEmpty())) {
//            call.respond(HttpStatusCode.Unauthorized, "Token not found")
//        } else {
//            val RegisterDTO = Registers.fetchForAdress(registerReceiveRemote.adress)
//            if (RegisterDTO == null) {
//                call.respond(HttpStatusCode.NotFound, "Register not found")
//            } else {
//                call.respond(
//                    RegisterResponseRemote(
//                        adress = RegisterDTO.adress,
//                        name = RegisterDTO.name,
//                        value = RegisterDTO.value,
//                        unit = RegisterDTO.unit,
//                        timestamp = RegisterDTO.timestamp,
//                        owner = RegisterDTO.owner
//                    )
//                )
//            }
//        }
//
//
//    }
}