package com.nevmem.survey.setup

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.service.users.UsersService
import io.ktor.application.Application
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject

@DelicateCoroutinesApi
fun Application.initializeAdminAccount() {
    val usersService by inject<UsersService>()
    val password = EnvVars.Admin.password
    if (password != null) {
        GlobalScope.launch {
            usersService.createUser(
                UsersService.Credentials(
                    "admin",
                    password,
                ),
                UsersService.Personal(
                    "admin",
                    "admin",
                    "email@admin.com",
                )
            )

        }
    }
}
