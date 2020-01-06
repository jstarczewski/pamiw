package com.jstarczewski.updf.db

import java.io.Serializable

data class Publication(val id: Long,
                       val author: String,
                       val title: String,
                       val description: String,
                       val pdfIds: List<Long>?) : Serializable