package com.jstarczewski.log.db

interface UserDataSource {

    fun userByLogin(login: String): User?

    fun userById(id: String): User?

    fun user(login: String, password: String): User?
}