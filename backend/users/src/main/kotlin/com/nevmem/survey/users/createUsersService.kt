package com.nevmem.survey.users

import com.nevmem.survey.users.internal.UsersServiceImpl
import com.nevmem.survey.users.internal.UsersTable
import org.jetbrains.exposed.sql.Table

fun createUsersService(): UsersService = UsersServiceImpl()

fun usersTables(): List<Table> = listOf(UsersTable)
