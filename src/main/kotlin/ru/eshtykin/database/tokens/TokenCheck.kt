package ru.eshtykin.database.tokens

import ru.eshtykin.database.tokens.admin.AdminTokens
import ru.eshtykin.constatns.private.Constants

object TokenCheck {

//    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens()?.firstOrNull{ it.token == token} != null
    fun isTokenValid(token: String): Boolean = Tokens.fetch(token) != null
    fun isTokenAdmin(token: String): Boolean = AdminTokens.fetch(token) != null
}