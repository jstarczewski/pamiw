package com.jstarczewski.log.publication.api

import java.io.Serializable

data class PdfAction(
    val file: Action,
    val delete: Action
) : Serializable