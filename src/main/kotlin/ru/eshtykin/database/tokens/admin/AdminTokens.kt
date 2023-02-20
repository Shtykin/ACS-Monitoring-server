package ru.eshtykin.database.tokens.admin

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.eshtykin.database.users.UserDTO
import ru.eshtykin.database.users.Users

object AdminTokens : Table("admin_tokens") {
    private val id = AdminTokens.varchar("id", 75)
    private val login = AdminTokens.varchar("login", 25)
    private val token = AdminTokens.varchar("token", 75)

    fun insert(amninTokenDTO: AdminTokenDTO) {
        transaction {
            AdminTokens.insert {
                it[id] = amninTokenDTO.id
                it[login] = amninTokenDTO.login
                it[token] = amninTokenDTO.token
            }
        }
    }

    fun fetch(adminToken: String): AdminTokenDTO? {
        return try {
            transaction {
                val tokenModel = AdminTokens.select { token.eq(adminToken) }.first()
                AdminTokenDTO(
                    id = tokenModel[AdminTokens.id],
                    login = tokenModel[AdminTokens.login],
                    token = tokenModel[AdminTokens.token],
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchTokens(): List<AdminTokenDTO>? {
        return try {
            transaction {
                AdminTokens.selectAll().toList()
                    .map {
                        AdminTokenDTO(
                            id = it [AdminTokens.id],
                            login = it [login],
                            token = it [token]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

}