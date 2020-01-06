package com.jstarczewski.log.publication.api

import com.jstarczewski.mlog.model.publication.api.Action
import java.io.Serializable

data class PdfAction(
    val file: Action,
    val delete: Action
) : Serializable