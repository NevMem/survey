package com.nevmem.survey.plugins

import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.env.EnvVars
import io.ktor.application.Application
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokenService by inject<TokenService>()

    authentication {
        jwt {
            val jwtAudience = EnvVars.JWT.audience
            realm = EnvVars.JWT.realm
            verifier(tokenService.createJWTVerifier())
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
