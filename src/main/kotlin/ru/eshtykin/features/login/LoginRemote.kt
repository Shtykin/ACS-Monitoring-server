package ru.eshtykin.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote (
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseRemote (
    val token: String
)

@Serializable
data class LoginAdminReceiveRemote (
    val login: String,
    val password: String
)

@Serializable
data class LoginAdminResponseRemote (
    val token: String
)