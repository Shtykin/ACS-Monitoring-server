package ru.eshtykin.database.owners

object OwnerCheck {
    fun isValidOwner(login: String, owner: String?) =
        Owners.fetchByLogin(login)?.firstOrNull{ it.owner == owner} != null
}