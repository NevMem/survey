package com.nevmem.survey.auth.internal

import com.nevmem.survey.auth.PasswordEncoder
import com.nevmem.survey.env.EnvVars
import java.security.MessageDigest

internal class PasswordEncoderImpl : PasswordEncoder {
    override fun encodePassword(password: String): String = (password + EnvVars.Security.salt).sha256()

    private fun String.sha256(): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}
