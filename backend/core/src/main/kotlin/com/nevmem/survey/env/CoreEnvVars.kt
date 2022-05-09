package com.nevmem.survey.env

object CoreEnvVars {
    object ACME {
        val value: String by lazy { env("ACME_BACKEND_CHALLENGE_VALUE") }
        val key: String by lazy { env("ACME_BACKEND_CHALLENGE_KEY") }
    }
}
