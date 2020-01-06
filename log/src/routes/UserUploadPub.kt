package com.jstarczewski.log.routes

import com.jstarczewski.log.UserPage
import com.jstarczewski.log.UserUploadPub
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.defaultPath
import com.jstarczewski.log.helpers.withSession
import com.jstarczewski.log.util.auth
import com.jstarczewski.log.util.redirect
import com.jstarczewski.log.util.token
import io.ktor.application.call
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing

private const val AUTHOR = "author"
private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val PDF_IDS = "pdfIds"

private const val EMPTY_ERROR = "Fields cannot be empty"

fun Routing.userUploadPub(db: UserDataSource, client: io.ktor.client.HttpClient) {

    post<UserUploadPub> {
        withSession(db) {
            val post = call.receive<Parameters>()
            val author = db.userById(it.userId)?.login
            val title = post[TITLE]
            val description = post[DESCRIPTION]
            if (author?.isNotEmpty() == true
                && title?.isNotEmpty() == true
                && description?.isNotEmpty() == true
            ) {
                client.submitForm {
                    header(auth(), token(it.login))
                    url("${defaultPath}pub")
                    body = MultiPartFormDataContent(
                        formData {
                            append(AUTHOR, author)
                            append(TITLE, title)
                            append(DESCRIPTION, description)
                            append(PDF_IDS, "")
                        }
                    )
                    call.redirect(UserPage())
                }
            } else {
                call.redirect(UserUploadPub(EMPTY_ERROR))
            }
        } ?: call.respond(HttpStatusCode.Forbidden)
    }

    get<UserUploadPub> {
        withSession(db) { user ->
            call.respond(
                FreeMarkerContent(
                    "publication.ftl", mapOf(
                        "userId" to user.userId,
                        "user" to user,
                        "error" to it.error
                    ), ""
                )
            )
        } ?: call.respond(HttpStatusCode.Forbidden)
    }
}