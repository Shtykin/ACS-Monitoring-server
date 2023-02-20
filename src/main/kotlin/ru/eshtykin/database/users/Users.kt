package ru.eshtykin.database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.eshtykin.database.registers.RegisterDTO
import ru.eshtykin.database.registers.Registers

object Users : Table("users") {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 25)
    private val email = Users.varchar("email", 25).nullable()
    private val role = Users.varchar("role", 25).nullable()

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email
                it[role] = userDTO.role
            }
        }
    }

    fun fetch(userLogin: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { login.eq(userLogin) }.first()
                UserDTO(
                    login = userModel[login],
                    password = userModel[password],
                    email = userModel[email],
                    role = userModel[role]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAll(): List<UserDTO>? {
        return try {
            transaction {
                Users.selectAll().toList()
                    .map {
                        UserDTO(
                            login = it[login],
                            password = it[password],
                            email = it[email],
                            role = it[role]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun update(userDTO: UserDTO): Boolean {
        return try {
            transaction {
                Users.update({ Users.login eq userDTO.login }) {
                    it[role] = userDTO.role
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}