package ru.eshtykin.features.role

import kotlinx.serialization.Serializable

@Serializable
data class RoleReceiveRemote (
    val login: String,
    val role: String
)

@Serializable
data class RoleResponseRemote (
    val login: String,
    val role: String
)