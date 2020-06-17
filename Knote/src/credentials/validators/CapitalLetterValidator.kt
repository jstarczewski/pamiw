package com.jstarczewski.knote.credentials.validators

import com.jstarczewski.knote.credentials.Validator

class CapitalLetterValidator : Validator {

    companion object {

        private const val CAPITAL_LETTER_ERROR = "Password must contain at least one capital letter"
    }

    override fun validate(credential: String) =
        if (credential.matches(Regex(".*[A-Z].*"))) credential else null

    override fun getValidationErrorMessage() =
        CAPITAL_LETTER_ERROR
}