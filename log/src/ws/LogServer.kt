package com.jstarczewski.log.ws

import io.ktor.http.cio.websocket.WebSocketSession
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class LogServer {

    val usersCounter = AtomicInteger()

    /**
     * A concurrent map associating session IDs to user names.
     */
    val memberNames = ConcurrentHashMap<String, String>()

    /**
     * Associates a session-id to a set of websockets.
     * Since a browser is able to open several tabs and windows with the same cookies and thus the same session.
     * There might be several opened sockets for the same client.
     */
    val members = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    /**
     * A list of the lastest messages sent to the server, so new members can have a bit context of what
     * other people was talking about before joining.
     */
    val lastMessages = LinkedList<String>()
}