package com.nevmem.survey.env

object EnvVars {
    object DataSource {
        val user: String by lazy { env("DB_USERNAME") }
        val password: String by lazy { env("DB_PASSWORD") }
        val databaseName: String by lazy { env("DB_DBNAME") }
        val serverName: String by lazy { env("DB_SERVER") }
        val portNumber: String by lazy { env("DB_PORT") }
    }

    object JWT {
        val audience by lazy { env("JWT_AUDIENCE") }
        val realm by lazy { env("JWT_REALM") }
        val secret by lazy { env("JWT_SECRET") }
        val domain by lazy { env("JWT_DOMAIN") }
    }
}

private fun env(key: String): String {
    return System.getenv(key) ?: throw IllegalStateException("Environment variable with key $key not found")
}
