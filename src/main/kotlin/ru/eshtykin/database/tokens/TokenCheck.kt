package ru.eshtykin.database.tokens

import ru.eshtykin.private.Constants

object TokenCheck {

    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens()?.firstOrNull{ it.token == token} != null
    fun isTokenAdmin(token: String?): Boolean = token == Constants.adminToken
}