package com.nevmem.survey.worker.setup

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.media.mediaTables
import com.nevmem.survey.survey.answersTables
import com.nevmem.survey.survey.surveysTables
import com.nevmem.survey.task.tasksTables
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.Properties

fun setupDatabases() {
    Database.connect(
        HikariDataSource(
            HikariConfig(
                Properties().apply {
                    put("dataSource.user", EnvVars.DataSource.user)
                    put("dataSource.password", EnvVars.DataSource.password)
                    put("dataSource.databaseName", EnvVars.DataSource.databaseName)
                    put("dataSource.serverName", EnvVars.DataSource.serverName)
                    put("dataSource.portNumber", EnvVars.DataSource.portNumber)
                    put("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
                }
            )
        )
    )
    createTables()
    LoggerFactory.getLogger("setup-databases").info("Database initialized")
}

private fun createTables() = transaction {
    SchemaUtils.create(
        *surveysTables().toTypedArray(),
        *answersTables().toTypedArray(),
        *mediaTables().toTypedArray(),
        *tasksTables().toTypedArray(),
    )
}
