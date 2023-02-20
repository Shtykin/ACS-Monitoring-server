package ru.eshtykin.database.owners

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.eshtykin.database.users.UserDTO
import ru.eshtykin.database.users.Users

object Owners : Table("owners") {
    private val id = Owners.varchar("id", 75)
    private val login = Owners.varchar("login", 25)
    private val owner = Owners.varchar("owner", 50)

    fun insert(ownerDTO: OwnerDTO) {
        transaction {
            Owners.insert {
                it[id] = ownerDTO.id
                it[login] = ownerDTO.login
                it[owner] = ownerDTO.owner
            }
        }
    }

    fun fetch(): List<OwnerDTO>? {
        return try {
            transaction {
                Owners.selectAll().toList()
                    .map {
                        OwnerDTO(
                            id = it [Owners.id],
                            login = it [login],
                            owner = it [owner]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchByLogin(selectLogin: String): List<OwnerDTO>? {
        return try {
            transaction {
                Owners.select { login.eq(selectLogin) }.toList()
                    .map {
                        OwnerDTO(
                            id = it [Owners.id],
                            login = it [login],
                            owner = it [owner]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchByOwner(selectOwner: String): List<OwnerDTO>? {
        return try {
            transaction {
                Owners.select { owner.eq(selectOwner) }.toList()
                    .map {
                        OwnerDTO(
                            id = it [Owners.id],
                            login = it [login],
                            owner = it [owner]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

}