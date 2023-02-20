package ru.eshtykin.database.tokens.admin

data class AdminTokenDTO(
    val id: String,
    val login: String,
    val token: String
)
