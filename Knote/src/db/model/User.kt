package com.jstarczewski.knote.db.model

import java.io.Serializable

data class User(
    val userId: Long,
    val login: String,
    val password: String,
    val salt: ByteArray
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (login != other.login) return false
        if (password != other.password) return false
        if (!salt.contentEquals(other.salt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}