package ru.eshtykin.database.users

data class UserDTO(
    val login: String,
    val password: String,
    val email: String?,
    val role: String?
)
