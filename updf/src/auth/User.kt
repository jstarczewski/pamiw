package com.jstarczewski.updf.auth

import io.ktor.auth.Principal


data class User(val userName: String) : Principal