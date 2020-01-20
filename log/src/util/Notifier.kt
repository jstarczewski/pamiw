package com.jstarczewski.log.util

import com.jstarczewski.log.LogSession

class Notifier {

    val users = ArrayList<LogSession>()

    private val notifications = HashMap<LogSession, String>()

    fun addNotification(notification: String) {
        users.forEach {
            notifications[it] = notification
        }
    }

    fun getNotification(session: LogSession): String? =
        notifications[session].also {
            notifications.remove(session)
        }
}