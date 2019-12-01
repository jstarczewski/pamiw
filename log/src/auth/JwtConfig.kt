package com.jstarczewski.log.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {

    private const val claimName = "userName"

    private const val secret = "zAP5MBA4B4Ijz0MZaS48"
    private const val issuer = "WEBOWECZKA"
    private const val validityInMs = 1000 * 60 * 5
    private val algorithm = Algorithm.HMAC512(secret)

    fun makeToken(userName: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim(claimName, userName)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}
