package ru.eshtykin.features.registers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.constatns.publ.Constants
import ru.eshtykin.database.owners.OwnerCheck
import ru.eshtykin.database.registers.RegisterDTO
import ru.eshtykin.database.registers.Registers
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.tokens.Tokens
import ru.eshtykin.database.users.RoleCheck
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun readRegisterByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registerReceiveRemote = call.receive<ReadRegisterByExplorerReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleExplorer)) return
        if (!checkOwner(token, registerReceiveRemote.owner)) return

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAddress = registerReceiveRemote.address
            )

        if (register == null) {
            call.respond(HttpStatusCode.BadRequest, "Register not found")
            return
        }
        call.respond(register.mapToRegisterResponseRemote())
    }

    suspend fun readRegistersByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registersReceiveRemote = call.receive<ReadRegistersByExplorerReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleExplorer)) return
        if (!checkOwner(token, registersReceiveRemote.owner)) return

        val registers = Registers
            .fetchForOwner(registerOwner = registersReceiveRemote.owner)

        if (registers.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Registers not found")
            return
        }
        call.respond(registers.map { it.mapToRegisterResponseRemote() })
    }

    suspend fun setRegisterByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registerReceiveRemote = call.receive<SetRegisterByExplorerReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleExplorer)) return
        if (!checkOwner(token, registerReceiveRemote.owner)) return

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAddress = registerReceiveRemote.address
            )

        val registerDTO = RegisterDTO(
            id = UUID.randomUUID().toString(),
            address = registerReceiveRemote.address,
            name = registerReceiveRemote.name,
            value = null,
            unit = registerReceiveRemote.unit,
            timestamp = null,
            owner = registerReceiveRemote.owner
        )
        if (register == null) Registers.insert(registerDTO)
        else Registers.updateSettings(registerDTO)

        val newRegisterDTO = Registers.fetchForOwnerAndAddress(
            registerOwner = registerReceiveRemote.owner,
            registerAddress = registerReceiveRemote.address
        )
        if (newRegisterDTO == null) call.respond(HttpStatusCode.Conflict, "Failed to set settings")
        else call.respond(newRegisterDTO.mapToRegisterResponseRemote())
    }

    suspend fun setRegistersByExplorer() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registersReceiveRemote = call.receive<SetExplorerByDeviceReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleExplorer)) return
        if (!checkOwner(token, registersReceiveRemote.owner)) return

        val requestList = mutableListOf<RegisterResponseRemote>()
        registersReceiveRemote.registers.forEach { registerReceiveRemote ->
                val register = Registers
                    .fetchForOwnerAndAddress(
                        registerOwner = registersReceiveRemote.owner,
                        registerAddress = registerReceiveRemote.address
                    )
                val registerDTO = RegisterDTO(
                    id = UUID.randomUUID().toString(),
                    address = registerReceiveRemote.address,
                    name = registerReceiveRemote.name,
                    value = null,
                    unit = registerReceiveRemote.unit,
                    timestamp = null,
                    owner = registersReceiveRemote.owner
                )
                if (register == null) Registers.insert(registerDTO)
                else Registers.updateSettings(registerDTO)

                val newRegisterDTO = Registers.fetchForOwnerAndAddress(
                    registerOwner = registersReceiveRemote.owner,
                    registerAddress = registerReceiveRemote.address
                )
                newRegisterDTO?.let { requestList.add(it.mapToRegisterResponseRemote()) }
        }
        if (requestList.isEmpty()) call.respond(HttpStatusCode.Conflict, "Failed to set settings")
        else call.respond(requestList)
    }

    suspend fun setRegisterByDevice() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registerReceiveRemote = call.receive<SetRegisterByDeviceReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleDevice)) return
        if (!checkOwner(token, registerReceiveRemote.owner)) return

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAddress = registerReceiveRemote.address
            )

        val registerDTO = RegisterDTO(
            id = UUID.randomUUID().toString(),
            address = registerReceiveRemote.address,
            name = null,
            value = registerReceiveRemote.value,
            unit = null,
            timestamp = System.currentTimeMillis(),
            owner = registerReceiveRemote.owner
        )

        if (register == null) Registers.insert(registerDTO)
        else Registers.updateValue(registerDTO)

        val newRegisterDTO = Registers.fetchForOwnerAndAddress(
            registerOwner = registerReceiveRemote.owner,
            registerAddress = registerReceiveRemote.address
        )
        if (newRegisterDTO == null) call.respond(HttpStatusCode.Conflict, "Failed to set value")
        else call.respond(newRegisterDTO.mapToRegisterResponseRemote())
    }

    suspend fun setRegistersByDevice() {
        val token = call.request.headers["Baerer-Autorization"]
        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Token is empty")
            return
        }
        val registersReceiveRemote = call.receive<SetRegistersByDeviceReceiveRemote>()
        if (!checkToken(token)) return
        if (!checkRole(token, Constants.roleDevice)) return
        if (!checkOwner(token, registersReceiveRemote.owner)) return

        val requestList = mutableListOf<RegisterResponseRemote>()
        registersReceiveRemote.registers.forEach { registerReceiveRemote ->
                val register = Registers
                    .fetchForOwnerAndAddress(
                        registerOwner = registersReceiveRemote.owner,
                        registerAddress = registerReceiveRemote.address
                    )
                val registerDTO = RegisterDTO(
                    id = UUID.randomUUID().toString(),
                    address = registerReceiveRemote.address,
                    name = null,
                    value = registerReceiveRemote.value,
                    unit = null,
                    timestamp = System.currentTimeMillis(),
                    owner = registersReceiveRemote.owner
                )
                if (register == null) Registers.insert(registerDTO)
                else Registers.updateValue(registerDTO)

                val newRegisterDTO = Registers.fetchForOwnerAndAddress(
                    registerOwner = registersReceiveRemote.owner,
                    registerAddress = registerReceiveRemote.address
                )
                newRegisterDTO?.let { requestList.add(it.mapToRegisterResponseRemote()) }
        }
        if (requestList.isEmpty()) call.respond(HttpStatusCode.Conflict, "Failed to set values")
        else call.respond(requestList)
    }


    private suspend fun checkToken(token: String): Boolean =
        if (!TokenCheck.isTokenValid(token.orEmpty())) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            false
        } else true

    private suspend fun checkRole(token: String, role: String): Boolean {
        val login = Tokens.fetch(token)?.login
        if (login == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return false
        }
        if (!RoleCheck.isValidRole(login, role)) {
            call.respond(HttpStatusCode.BadRequest, "Wrong role")
            return false
        }
        return true
    }

    private suspend fun checkOwner(token: String, owner: String?): Boolean {
        val login = Tokens.fetch(token)?.login
        if (login == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return false
        }
        if (!OwnerCheck.isValidOwner(login, owner)) {
            call.respond(HttpStatusCode.BadRequest, "User is not owner")
            return false
        }
        return true
    }
}