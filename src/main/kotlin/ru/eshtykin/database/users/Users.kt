package ru.eshtykin.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 25)
    private val email = Users.varchar("email", 25).nullable()

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email
            }
        }
    }

    fun fetch(userLogin: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { Users.login.eq(userLogin) }.first()
                UserDTO(
                    login = userModel[login],
                    password = userModel[password],
                    email = userModel[email],
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}