package com.jstarczewski.knote.credentials.validators

import com.jstarczewski.knote.credentials.Validator

class WhitespaceValidator : Validator {

    companion object {

        private const val WHITE_SPACE_ERROR = "Credential cannot contain whitespaces"
    }

    override fun validate(credential: String): String? =
        if (credential.matches(Regex("\\s+")).not()) credential else null

    override fun getValidationErrorMessage(): String =
        WHITE_SPACE_ERROR
}