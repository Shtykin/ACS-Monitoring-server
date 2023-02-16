package ru.eshtykin.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table("tokens") {
    private val id = Tokens.varchar("id", 75)
    private val login = Tokens.varchar("login", 25)
    private val token = Tokens.varchar("token", 75)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDTO>? {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            id = it [Tokens.id],
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