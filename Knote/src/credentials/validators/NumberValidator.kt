package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.credentials.Validator

class NumberValidator : Validator {

    companion object {

        private const val NUMBER_ERROR = "Password must contain number"
    }

    override fun validate(credential: String): String? =
        if (credential.matches(Regex(".*\\d.*"))) credential else null

    override fun getValidationErrorMessage(): String =
        NUMBER_ERROR
}