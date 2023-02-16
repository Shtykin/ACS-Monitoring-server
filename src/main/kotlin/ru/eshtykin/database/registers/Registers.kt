package ru.eshtykin.database.registers

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Registers : Table("registers") {
    private val address = Registers.integer("adress")
    private val name = Registers.varchar("name", 100).nullable()
    private val value = Registers.varchar("value", 25).nullable()
    private val unit = Registers.varchar("unit", 25).nullable()
    private val timestamp = Registers.long("timestamp").nullable()
    private val owner = Registers.varchar("owner", 50).nullable()

    fun insert(registerDTO: RegisterDTO) {
        transaction {
            Registers.insert {
                it[address] = registerDTO.address
                it[name] = registerDTO.name
                it[value] = registerDTO.value
                it[unit] = registerDTO.unit
                it[timestamp] = registerDTO.timestamp
                it[owner] = registerDTO.owner
            }
        }
    }

    fun updateSettings(registerDTO: RegisterDTO) {
        transaction {
            Registers.update( {address.eq(registerDTO.address) and owner.eq(registerDTO.owner)} ) {
                it[name] = registerDTO.name
                it[unit] = registerDTO.unit
            }
        }
    }
    fun updateValue(registerDTO: RegisterDTO) {
        transaction {
            Registers.update( {address.eq(registerDTO.address) and owner.eq(registerDTO.owner)} ) {
                it[value] = registerDTO.value
            }
        }
    }

    fun fetchForAdress(registerAdress: Int): RegisterDTO? {
        return try {
            transaction {
                val registerModel = Registers.select { address.eq(registerAdress) }.first()
                RegisterDTO(
                    address = registerModel[address],
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
                            address = it[address],
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

    fun fetchForOwnerAndAddress(registerOwner: String?, registerAdress: Int): RegisterDTO? {
        return try {
            transaction {
                val registerModel = Registers
                    .select { address.eq(registerAdress) and owner.eq(registerOwner) }
                    .first()
                RegisterDTO(
                    address = registerModel[address],
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

    fun fetchAll(): List<RegisterDTO>? {
        return try {
            transaction {
                Registers.selectAll().toList()
                    .map {
                        RegisterDTO(
                            address = it[address],
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