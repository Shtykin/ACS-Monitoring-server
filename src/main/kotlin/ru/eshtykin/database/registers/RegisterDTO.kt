package ru.eshtykin.database.registers

data class RegisterDTO(
    val id: String,
    val address: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)
