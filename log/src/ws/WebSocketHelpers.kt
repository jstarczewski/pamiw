package com.jstarczewski.log.ws

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import kotlinx.coroutines.channels.ClosedSendChannelException


suspend fun List<WebSocketSession>.send(frame: Frame) {
    forEach {
        try {
            it.send(frame.copy())
        } catch (t: Throwable) {
            try {
                it.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, ""))
            } catch (ignore: ClosedSendChannelException) {
                // at some point it will get closed
            }
        }
    }
}