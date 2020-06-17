package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.credentials.Validator

class LengthValidator : Validator {

    companion object {

        private const val LENGTH_ERROR = "Password is too short"
        private const val MIN_LENGTH = 8
    }

    override fun validate(credential: String): String? =
        if (credential.length >= MIN_LENGTH) credential else null

    override fun getValidationErrorMessage(): String =
        LENGTH_ERROR
}