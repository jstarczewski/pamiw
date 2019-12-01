package com.jstarczewski.log.db

import java.io.Serializable

data class Pdf(val id: Long, val fileName: String, val author: String) : Serializable