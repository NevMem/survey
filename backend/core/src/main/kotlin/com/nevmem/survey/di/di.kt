package com.nevmem.survey.di

import com.nevmem.survey.role.RoleConverter
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.internal.RoleConverterImpl
import com.nevmem.survey.role.internal.mainRoleModel
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.internal.UsersServiceImpl
import io.ktor.application.*
import io.ktor.application.Application
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

private val coreModule = module {
    single<RoleModel> { mainRoleModel() }
    single<RoleConverter> { RoleConverterImpl(get()) }
}

private val servicesModule = module {
    single<UsersService> { UsersServiceImpl() }
}

fun Application.di() {
    install(Koin) {
        modules(coreModule, servicesModule)
    }
}
