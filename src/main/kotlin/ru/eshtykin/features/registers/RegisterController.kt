package ru.eshtykin.features.registers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.eshtykin.database.registers.RegisterDTO
import ru.eshtykin.database.registers.Registers
import ru.eshtykin.database.tokens.TokenCheck
import ru.eshtykin.database.tokens.Tokens
import ru.eshtykin.database.users.RoleCheck
import ru.eshtykin.private.Constants

class RegisterController(private val call: ApplicationCall) {

    suspend fun readRegisterByExplorer() {
        if (!checkUser(Constants.roleExplorer)) return

        val registerReceiveRemote = call.receive<ReadRegisterByExplorerReceiveRemote>()

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAdress = registerReceiveRemote.address
            )

        if (register == null) {
            call.respond(HttpStatusCode.BadRequest, "Register not found")
            return
        }
        call.respond(register.mapToRegisterResponseRemote())
    }

    suspend fun readRegistersByExplorer() {
        if (!checkUser(Constants.roleExplorer)) return

        val registersReceiveRemote = call.receive<ReadRegistersByExplorerReceiveRemote>()

        val registers = Registers
            .fetchForOwner(registerOwner = registersReceiveRemote.owner)

        if (registers.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Registers not found")
            return
        }
        call.respond(registers.map { it.mapToRegisterResponseRemote() })
    }

    suspend fun setRegisterByExplorer() {
        if (!checkUser(Constants.roleExplorer)) return
        val registerReceiveRemote = call.receive<SetRegisterByExplorerReceiveRemote>()

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAdress = registerReceiveRemote.address
            )

        val registerDTO = RegisterDTO(
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
            registerAdress = registerReceiveRemote.address
        )
        if (newRegisterDTO == null) call.respond(HttpStatusCode.Conflict, "Failed to set settings")
        else call.respond(newRegisterDTO.mapToRegisterResponseRemote())
    }

    suspend fun setRegistersByExplorer() {
        if (!checkUser(Constants.roleExplorer)) return
        val registersReceiveRemote = call.receive<SetRegistersByExplorerReceiveRemote>()

        val requestList = mutableListOf<RegisterResponseRemote>()
        registersReceiveRemote.registers.forEach {registerReceiveRemote ->
            val register = Registers
                .fetchForOwnerAndAddress(
                    registerOwner = registerReceiveRemote.owner,
                    registerAdress = registerReceiveRemote.address
                )
            val registerDTO = RegisterDTO(
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
                registerAdress = registerReceiveRemote.address
            )
            newRegisterDTO?.let { requestList.add(it.mapToRegisterResponseRemote()) }

        }
        if (requestList.isEmpty()) call.respond(HttpStatusCode.Conflict, "Failed to set settings")
        else call.respond(requestList)
    }

    suspend fun setRegisterByDevice() {
        if (!checkUser(Constants.roleDevice)) return
        val registerReceiveRemote = call.receive<SetRegisterByDeviceReceiveRemote>()

        val register = Registers
            .fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAdress = registerReceiveRemote.address
            )

        val registerDTO = RegisterDTO(
            address = registerReceiveRemote.address,
            name = null,
            value = registerReceiveRemote.value,
            unit = null,
            timestamp = null,
            owner = registerReceiveRemote.owner
        )

        if (register == null) Registers.insert(registerDTO)
        else Registers.updateValue(registerDTO)

        val newRegisterDTO = Registers.fetchForOwnerAndAddress(
            registerOwner = registerReceiveRemote.owner,
            registerAdress = registerReceiveRemote.address
        )
        if (newRegisterDTO == null) call.respond(HttpStatusCode.Conflict, "Failed to set value")
        else call.respond(newRegisterDTO.mapToRegisterResponseRemote())
    }

    suspend fun setRegistersByDevice() {
        if (!checkUser(Constants.roleDevice)) return
        val registersReceiveRemote = call.receive<SetRegistersByDeviceReceiveRemote>()

        val requestList = mutableListOf<RegisterResponseRemote>()
        registersReceiveRemote.registers.forEach {registerReceiveRemote ->
            val register = Registers
                .fetchForOwnerAndAddress(
                    registerOwner = registerReceiveRemote.owner,
                    registerAdress = registerReceiveRemote.address
                )
            val registerDTO = RegisterDTO(
                address = registerReceiveRemote.address,
                name = null,
                value = registerReceiveRemote.value,
                unit = null,
                timestamp = null,
                owner = registerReceiveRemote.owner
            )
            if (register == null) Registers.insert(registerDTO)
            else Registers.updateValue(registerDTO)

            val newRegisterDTO = Registers.fetchForOwnerAndAddress(
                registerOwner = registerReceiveRemote.owner,
                registerAdress = registerReceiveRemote.address
            )
            newRegisterDTO?.let { requestList.add(it.mapToRegisterResponseRemote()) }

        }
        if (requestList.isEmpty()) call.respond(HttpStatusCode.Conflict, "Failed to set values")
        else call.respond(requestList)
    }

    private suspend fun checkUser(role: String): Boolean {
        val token = call.request.headers["Baerer-Autorization"]
        if (!TokenCheck.isTokenValid(token.orEmpty()) || token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return false
        }
        val login = Tokens.fetch(token)?.login
        if (login == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return false
        }
        if (RoleCheck.getRole(login) != role) {
            call.respond(HttpStatusCode.BadRequest, "Wrong role")
            return false
        }
        return true
    }
}