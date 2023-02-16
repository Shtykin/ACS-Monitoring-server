package ru.eshtykin.database.registers

import ru.eshtykin.database.registers.Registers.nullable

data class RegisterDTO(
    val adress: Int,
    val name: String?,
    val value: String?,
    val unit: String?,
    val timestamp: Long?,
    val owner: String?
)
