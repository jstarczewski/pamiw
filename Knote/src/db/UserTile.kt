package com.jstarczewski.knote.db

import com.jstarczewski.knote.db.model.User

data class UserTile(
    val userId: Long,
    val login: String
) {
    companion object {

        fun from(user: User) =
            UserTile(user.userId, user.login)
    }
}