package com.jstarczewski.knote.db.user

import com.jstarczewski.knote.db.model.User

interface UserDataSource {

    fun userByLogin(login: String): User?

    fun userById(id: Long): User?

    fun saveUser(login: String, password: String, salt: ByteArray): Boolean

    fun user(login: String, password: String): User?

    fun changePassword(id: Long, login: String, password: String, salt: ByteArray)
}