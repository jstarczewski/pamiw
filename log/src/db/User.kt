package com.jstarczewski.log.db

import java.io.Serializable

data class User(val userId: String, val login: String, val password: String) : Serializable