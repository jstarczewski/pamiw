package com.jstarczewski.log.db

import com.jstarczewski.log.util.tryOrNull
import java.io.File

class UserDatabase(private val uploadDir: File) : CacheUserDatabase(uploadDir), UserDataSource {

    companion object {

        private const val USER_ID = "1"
        private const val USER_NAME = "bchaber"
        private const val PASSWORD = "123456789"
        private const val NOTE_JSON_FILE_APPENDIX = ".idx"
    }

    override fun userByLogin(login: String) =
        allIds
            .asSequence()
            .mapNotNull { userById(it) }
            .find { it.login == login }

    override fun userById(id: Long) = cache.get(id) ?: tryOrNull {
        gson.fromJson(File(uploadDir, "$id.idx").readText(), User::class.java)
            ?.also {
                cache.put(id, it)
            }
    }

    override fun saveUser(login: String, password: String): Boolean {
        val id = nextId()
        val user = User(id.toString(), login, password)
        File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).writeText(gson.toJson(user))
        allIds.add(id)
        cache.put(id, user)
        return true
    }

    override fun user(login: String, password: String) = allIds
        .asSequence()
        .mapNotNull { userById(it) }
        .find { it.login == login && it.password == password }

    override fun changePassword(id: Long, login: String, password: String) {
        val user = User(id.toString(), login, password)
        File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).writeText(gson.toJson(user))
        cache.replace(id, user)
    }
}