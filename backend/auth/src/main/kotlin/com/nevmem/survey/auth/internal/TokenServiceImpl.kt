package com.nevmem.survey.auth.internal

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.user.UserEntity

internal class TokenServiceImpl : TokenService {
    override fun createJWTVerifier(): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(EnvVars.JWT.secret))
            .withAudience(EnvVars.JWT.audience)
            .withIssuer(EnvVars.JWT.domain)
            .withClaimPresence("user_id")
            .withClaimPresence("user_name")
            .build()
    }

    override fun createTokenForUser(user: UserEntity): String {
        return JWT.create()
            .withAudience(EnvVars.JWT.audience)
            .withIssuer(EnvVars.JWT.domain)
            .withClaim("user_name", user.login)
            .withClaim("user_id", user.id.toString())
            .sign(Algorithm.HMAC256(EnvVars.JWT.secret))
    }
}
