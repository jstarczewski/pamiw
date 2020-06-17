package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.credentials.Validator

class LetterValidator : Validator {

    companion object {

        private const val LETTER_VALIDATOR = "Password must contain lower-case letter"
    }

    override fun validate(credential: String) =
        if (credential.matches(Regex(".*[a-z].*"))) credential else null

    override fun getValidationErrorMessage() =
        LETTER_VALIDATOR
}