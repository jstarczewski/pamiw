package com.jstarczewski.knote.credentials.validators

import com.jstarczewski.knote.credentials.Validator

class EmptyValidator : Validator {

    companion object {

        private const val EMPTY_CREDENTIAL_ERROR = "Credential cannot be empty"
    }

    override fun validate(credential: String) =
        if (credential.isEmpty().not()) credential else null

    override fun getValidationErrorMessage() = EMPTY_CREDENTIAL_ERROR
}