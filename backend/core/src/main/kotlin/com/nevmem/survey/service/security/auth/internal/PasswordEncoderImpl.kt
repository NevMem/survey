package com.nevmem.survey.service.security.auth.internal

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.service.security.auth.PasswordEncoder
import java.security.MessageDigest

internal class PasswordEncoderImpl : PasswordEncoder {
    override fun encodePassword(password: String): String = (password + EnvVars.Security.salt).sha256()

    private fun String.sha256(): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}
