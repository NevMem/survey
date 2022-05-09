package com.nevmem.survey.env

object CoreEnvVars {
    object ACME {
        val value: String? by lazy { maybeEnv("ACME_BACKEND_CHALLENGE_VALUE") }
        val key: String? by lazy { maybeEnv("ACME_BACKEND_CHALLENGE_KEY") }
    }
}
