package com.jstarczewski.knote.db.user

import com.jstarczewski.knote.db.model.User
import com.jstarczewski.knote.util.tryOrNull
import java.io.File

class UserDatabase(private val uploadDir: File) : CacheUserDatabase(uploadDir), UserDataSource {

    companion object {

        private const val USER_ID = 1L
        private const val USER_NAME = "bchaber"
        private const val PASSWORD = "123456789"
        private const val NOTE_JSON_FILE_APPENDIX = ".idx"
    }

    private val users = listOf(
        User(
            USER_ID,
            USER_NAME,
            PASSWORD
            , "".toByteArray()
        )
    )

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

    override fun saveUser(login: String, password: String, salt: ByteArray): Boolean {
        val id = nextId()
        val user = User(id, login, password, salt)
        File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).writeText(gson.toJson(user))
        allIds.add(id)
        cache.put(id, user)
        return true
    }

    override fun user(login: String, password: String) = allIds
        .asSequence()
        .mapNotNull { userById(it) }
        .find { it.login == login && it.password == password }

    override fun changePassword(id: Long, login: String, password: String, salt: ByteArray) {
        val user = User(id, login, password, salt)
        File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).writeText(gson.toJson(user))
        cache.replace(id, user)
    }
}