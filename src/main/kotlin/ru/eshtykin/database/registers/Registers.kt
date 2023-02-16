package ru.eshtykin.database.registers

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Table.Dual.nullable
import org.jetbrains.exposed.sql.transactions.transaction
import ru.eshtykin.database.users.UserDTO
import ru.eshtykin.database.users.Users

object Registers : Table("registers") {
    private val adress = Registers.integer("adress")
    private val name = Registers.varchar("name", 100).nullable()
    private val value = Registers.varchar("value", 25).nullable()
    private val unit = Registers.varchar("unit", 25).nullable()
    private val timestamp = Registers.long("timestamp").nullable()
    private val owner = Registers.varchar("owner", 50).nullable()

    fun insert(registerDTO: RegisterDTO) {
        transaction {
            Registers.insert {
                it[adress] = registerDTO.adress
                it[name] = registerDTO.name
                it[value] = registerDTO.value
                it[unit] = registerDTO.unit
                it[timestamp] = registerDTO.timestamp
                it[owner] = registerDTO.owner
            }
        }
    }

    fun update(registerDTO: RegisterDTO) {
        transaction {
            Registers.update({ Registers.adress eq registerDTO.adress }) {
                it[name] = registerDTO.name
                it[value] = registerDTO.value
                it[unit] = registerDTO.unit
                it[timestamp] = registerDTO.timestamp
                it[owner] = registerDTO.owner
            }
        }
    }

    fun fetchForAdress(registerAdress: Int): RegisterDTO? {
        return try {
            transaction {
                val registerModel = Registers.select { adress.eq(registerAdress) }.first()
                RegisterDTO(
                    adress = registerModel[adress],
                    name = registerModel[name],
                    value = registerModel[value],
                    unit = registerModel[unit],
                    timestamp = registerModel[timestamp],
                    owner = registerModel[owner]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchForOwner(registerOwner: String?): List<RegisterDTO>? {
        return try {
            transaction {
                Registers.select { owner.eq(registerOwner) }.toList()
                    .map {
                        RegisterDTO(
                            adress = it[adress],
                            name = it[name],
                            value = it[value],
                            unit = it[unit],
                            timestamp = it[timestamp],
                            owner = it[Registers.owner]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAll(): List<RegisterDTO>? {
        return try {
            transaction {
                Registers.selectAll().toList()
                    .map {
                        RegisterDTO(
                            adress = it[adress],
                            name = it[name],
                            value = it[value],
                            unit = it[unit],
                            timestamp = it[timestamp],
                            owner = it[owner]
                        )
                    }
            }
        } catch (e: Exception) {
            null
        }
    }

}