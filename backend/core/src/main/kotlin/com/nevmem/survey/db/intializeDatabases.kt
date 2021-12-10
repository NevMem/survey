package com.nevmem.survey.db

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.service.invites.internal.InvitesTable
import com.nevmem.survey.service.users.internal.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import java.util.Properties
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun Application.initializeDatabases() {
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
    LoggerFactory.getLogger(Application::class.simpleName).info("Database initialized")
}

private fun createTables() = transaction {
    SchemaUtils.create(
        UsersTable,
        InvitesTable,
    )
}
