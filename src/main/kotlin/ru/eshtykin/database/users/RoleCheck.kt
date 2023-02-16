package ru.eshtykin.database.users

object RoleCheck {

    fun getRole(login: String) = Users.fetch(login)?.role

}