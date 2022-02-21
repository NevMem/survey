package com.nevmem.survey.service.auth

import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.auth.internal.TokenServiceImpl

fun createTokenService(): TokenService = TokenServiceImpl()
