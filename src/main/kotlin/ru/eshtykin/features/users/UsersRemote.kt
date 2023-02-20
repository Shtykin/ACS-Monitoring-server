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
    val role: String?,
    val owner: List<String>?
)

@Serializable
data class OwnerReceiveRemote (
    val login: String,
    val owner: String
)

@Serializable
data class OwnerResponseRemote (
    val login: String,
    val owner: String
)