package ru.eshtykin.database.users

object RoleCheck {

    fun getRole(login: String) = Users.fetch(login)?.role

    fun isValidRole(login: String, role: String) =
        Users.fetch(login)?.role == role

}