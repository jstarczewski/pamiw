package com.jstarczewski.knote.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

class HashGenerator {

    companion object {

        private const val ALGORITHM = "SHA-512"
        private const val STRING_FORMAT = "%02x"
        private const val BYTE_ARRAY_SIZE = 16
        private const val HASH_ITERATIONS = 16
    }

    private val messageDigest = MessageDigest.getInstance(ALGORITHM)
    private val secureRandom = SecureRandom()

    private fun generateRandomSalt(): ByteArray {
        val salt = ByteArray(BYTE_ARRAY_SIZE)
        secureRandom.nextBytes(salt)
        return salt
    }

    fun getHashedPassword(password: String): Pair<String, ByteArray> {
        val salt = generateRandomSalt()
        messageDigest.update(salt)
        var hashedPassword = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        for (x in 0..HASH_ITERATIONS) {
            hashedPassword = messageDigest.digest(hashedPassword)
        }
        val stringBuilder = StringBuilder()
        hashedPassword.iterator().forEach {
            stringBuilder.append(String.format(STRING_FORMAT, it))
        }
        return stringBuilder.toString() to salt
    }

    fun checkPasswords(password: String, salt: ByteArray, hashedPassword: String): Boolean {
        messageDigest.update(salt)
        var newHashedPassword = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        for (x in 0..HASH_ITERATIONS) {
            newHashedPassword = messageDigest.digest(newHashedPassword)
        }
        val stringBuilder = StringBuilder()
        newHashedPassword.iterator().forEach {
            stringBuilder.append(String.format(STRING_FORMAT, it))
        }
        return hashedPassword == stringBuilder.toString()
    }
}