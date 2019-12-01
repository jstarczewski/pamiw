package com.jstarczewski.updf.db.base

import java.util.concurrent.atomic.AtomicLong

interface IdHelper {

    val allIds: ArrayList<Long>

    val biggestId : AtomicLong

    fun nextId() : Long
}