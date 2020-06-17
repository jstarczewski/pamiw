package com.jstarczewski.knote.credentials.validators

import com.jstarczewski.knote.credentials.Validator

class PlainTextAndNumbersValidator : Validator {

    companion object {

        private const val PLAINTEXT_ERROR = "Login must contain only letters and numbers"
    }

    override fun validate(credential: String): String? =
        if (credential.matches(Regex("^[a-zA-Z0-9]*\$"))) credential else null

    override fun getValidationErrorMessage() = PLAINTEXT_ERROR
}