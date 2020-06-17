package com.jstarczewski.knote.credentials

interface Validator {

    fun validate(credential: String): String?

    fun getValidationErrorMessage(): String
}