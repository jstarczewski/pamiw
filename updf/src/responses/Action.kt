package com.jstarczewski.updf.responses

import java.io.Serializable

data class Action(val href: String, val method: String) : Serializable