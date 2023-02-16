package ru.eshtykin.features.registers

import kotlinx.serialization.Serializable
import ru.eshtykin.database.registers.RegisterDTO

@Serializable
data class RegisterReceiveRemote (
    val address: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)

@Serializable
data class RegisterResponseRemote (
    val address: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)

@Serializable
data class ReadRegisterByExplorerReceiveRemote (
    val address: Int,
    val owner: String?
)

@Serializable
data class ReadRegistersByExplorerReceiveRemote (
    val owner: String?
)

@Serializable
data class SetRegisterByExplorerReceiveRemote (
    val address: Int,
    val name: String?,
    val unit: String?,
    val owner: String?
)

@Serializable
data class SetRegisterByDeviceReceiveRemote (
    val address: Int,
    val value: String?,
    val owner: String?
)

@Serializable
data class SetRegistersByExplorerReceiveRemote (
    val registers: List<SetRegisterByExplorerReceiveRemote>
)

@Serializable
data class SetRegistersByDeviceReceiveRemote (
    val registers: List<SetRegisterByDeviceReceiveRemote>
)

@Serializable
data class FetchRegisterRequest (
    val token: String
)

fun RegisterDTO.mapToRegisterResponseRemote(): RegisterResponseRemote{
    return RegisterResponseRemote(
        address = this.address,
        name = this.name,
        value = this.value,
        unit = this.unit,
        timestamp = this.timestamp,
        owner = this.owner
    )
}