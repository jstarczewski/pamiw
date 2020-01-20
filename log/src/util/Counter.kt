package com.jstarczewski.log.util

class Counter {

    private var init = 0

    fun next() = init++

    fun clear() {
        init = 0
    }
}