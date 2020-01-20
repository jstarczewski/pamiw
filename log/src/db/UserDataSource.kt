package com.jstarczewski.log.db

interface UserDataSource {

    fun userByLogin(login: String): User?

    fun userById(id: Long): User?

    fun saveUser(login: String, password: String): Boolean

    fun user(login: String, password: String): User?

    fun changePassword(id: Long, login: String, password: String)
}