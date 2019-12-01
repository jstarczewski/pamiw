package com.jstarczewski.updf.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

object JwtConfig {

    const val claimName = "userName"

    private const val secret = "zAP5MBA4B4Ijz0MZaS48"
    private const val issuer = "WEBOWECZKA"
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()
}
