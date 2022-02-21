package com.nevmem.survey.auth

interface PasswordEncoder {
    fun encodePassword(password: String): String
}
