package com.nevmem.survey.service.security.auth

interface PasswordEncoder {
    fun encodePassword(password: String): String
}
