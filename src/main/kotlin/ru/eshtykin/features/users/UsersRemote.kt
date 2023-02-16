package ru.eshtykin.features.users

import kotlinx.serialization.Serializable

@Serializable
data class UserReceiveRemote (
    val login: String
)

@Serializable
data class UserResponseRemote (
    val login: String,
    val email: String?,
    val role: String?
)