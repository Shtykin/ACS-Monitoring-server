package ru.eshtykin.features.registers

import kotlinx.serialization.Serializable
import ru.eshtykin.database.registers.RegisterDTO
import ru.eshtykin.database.registers.Registers

@Serializable
data class RegisterReceiveRemote (
    val adress: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)

@Serializable
data class RegisterResponseRemote (
    val adress: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)

@Serializable
data class RegisterByExplorerReceiveRemote (
    val adress: Int,
    val owner: String?
)

@Serializable
data class RegistersByExplorerReceiveRemote (
    val owner: String?
)

@Serializable
data class FetchRegisterRequest (
    val token: String
)

fun RegisterDTO.mapToRegisterResponseRemote(): RegisterResponseRemote{
    return RegisterResponseRemote(
        adress = this.adress,
        name = this.name,
        value = this.value,
        unit = this.unit,
        timestamp = this.timestamp,
        owner = this.owner
    )
}