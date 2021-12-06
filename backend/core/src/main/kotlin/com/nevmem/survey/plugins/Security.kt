package com.nevmem.survey.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nevmem.survey.env.EnvVars
import io.ktor.application.Application
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt

fun Application.configureSecurity() {
    authentication {
        jwt {
            val jwtAudience = EnvVars.JWT.audience
            realm = EnvVars.JWT.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(EnvVars.JWT.secret))
                    .withAudience(jwtAudience)
                    .withIssuer(EnvVars.JWT.domain)
                    .withClaimPresence("user_id")
                    .withClaimPresence("user_name")
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
