package com.jstarczewski.mlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jstarczewski.mlog.model.publication.api.Action
import kotlinx.coroutines.launch

const val AUTHORIZATION = "Authorization"

fun auth() = AUTHORIZATION

fun token(login: String) = "Bearer ${JwtConfig.makeToken(login)}"

fun <T : Any> T.assign(block: T.() -> Unit) {
    this.block()
}

const val def =
    "http://10.0.2.2:8080"

fun Action.applyBaseUrl() = apply {
    href = def + this.href
}

fun ViewModel.async(block: suspend () -> Unit) {
    viewModelScope.launch {
        block()
    }
}