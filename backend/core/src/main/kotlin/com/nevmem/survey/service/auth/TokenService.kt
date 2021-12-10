package com.nevmem.survey.service.auth

import com.auth0.jwt.JWTVerifier
import com.nevmem.survey.service.users.data.UserEntity

interface TokenService {
    fun createJWTVerifier(): JWTVerifier
    fun createTokenForUser(user: UserEntity): String
}
