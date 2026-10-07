package com.metrolist.music.utils
class DiscordUser(val username: String, val name: String, val avatar: String?)
object KizzyRPC {
    fun getUserInfo(token: String, userAgent: String, superProperties: String): Result<DiscordUser> {
        return Result.failure(Exception("Not implemented"))
    }
}
object SuperProperties {
    val userAgent = ""
    val superPropertiesBase64 = ""
}
