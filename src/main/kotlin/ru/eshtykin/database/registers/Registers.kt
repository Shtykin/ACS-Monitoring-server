package ru.eshtykin.database.registers

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.eshtykin.database.tokens.Tokens

object Registers : Table("registers") {
    private val id = Registers.varchar("id", 75)
    private val address = Registers.integer("address")
    private val name = Registers.varchar("name", 100).nullable()
    private val value = Registers.varchar("value", 25).nullable()
    private val unit = Registers.varchar("unit", 25).nullable()
    private val timestamp = Registers.long("timestamp").nullable()
    private val owner = Registers.varchar("owner", 50).nullable()

    fun insert(registerDTO: RegisterDTO) {
        transaction {
            Registers.insert {
                it[id] = registerDTO.id
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
                it[timestamp] = registerDTO.timestamp
            }
        }
    }

    fun fetchForAddress(registerAddress: Int): RegisterDTO? {
        return try {
            transaction {
                val registerModel = Registers.select { address.eq(registerAddress) }.first()
                RegisterDTO(
                    id = registerModel[Registers.id],
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
                            id = it[Registers.id],
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

    fun fetchForOwnerAndAddress(registerOwner: String?, registerAddress: Int): RegisterDTO? {
        return try {
            transaction {
                val registerModel = Registers
                    .select { address.eq(registerAddress) and owner.eq(registerOwner) }
                    .first()
                RegisterDTO(
                    id = registerModel[Registers.id],
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
                            id = it[Registers.id],
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