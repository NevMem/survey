package com.nevmem.survey.env

object EnvVars {
    object Self {
        val uri: String by lazy { env("SELF_URI") }
    }

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

    object Security {
        val salt by lazy { env("SECURITY_SALT") }
    }

    object S3 {
        val region by lazy { env("S3_REGION") }
        val accessKey by lazy { env("S3_ACCESS_KEY") }
        val secretAccessKey by lazy { env("S3_SECRET_ACCESS_KEY") }
        val s3Uri by lazy { env("S3_URI") }
    }

    object Worker {
        val uri by lazy { maybeEnv("WORKER_URI") }
    }

    object Push {
        val uri by lazy { maybeEnv("PUSH_URI") }
    }
}

fun maybeEnv(key: String): String? = System.getenv(key)

fun env(key: String): String {
    return maybeEnv(key) ?: throw IllegalStateException("Environment variable with key $key not found")
}
