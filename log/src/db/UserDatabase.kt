package com.jstarczewski.log.db

class UserDatabase : UserDataSource {

    companion object {

        private const val USER_ID = "1"
        private const val USER_NAME = "bchaber"
        private const val PASSWORD = "123456789"
    }

    private val users = listOf(User(USER_ID, USER_NAME, PASSWORD))

    override fun userByLogin(login: String) = users.find { it.login == login }

    override fun userById(id: String) = users.find { it.userId == id }

    override fun user(login: String, password: String) = users.find { it.login == login && it.password == password }
}