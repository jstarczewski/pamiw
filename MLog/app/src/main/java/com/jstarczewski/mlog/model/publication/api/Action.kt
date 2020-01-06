package com.jstarczewski.mlog.model.publication.api

import java.io.Serializable

data class Action(var href: String, val method: String) : Serializable