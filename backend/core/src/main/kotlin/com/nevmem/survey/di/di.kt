package com.nevmem.survey.di

import com.nevmem.survey.role.RoleConverter
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.internal.RoleConverterImpl
import com.nevmem.survey.role.internal.mainRoleModel
import com.nevmem.survey.service.auth.TokenService
import com.nevmem.survey.service.auth.internal.TokenServiceImpl
import com.nevmem.survey.service.security.auth.PasswordEncoder
import com.nevmem.survey.service.security.auth.internal.PasswordEncoderImpl
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.internal.UsersServiceImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

private val coreModule = module {
    single<RoleModel> { mainRoleModel() }
    single<RoleConverter> { RoleConverterImpl(get()) }
    single<TokenService> { TokenServiceImpl() }
    single<PasswordEncoder> { PasswordEncoderImpl() }
}

private val servicesModule = module {
    single<UsersService> { UsersServiceImpl() }
}

fun Application.di() {
    install(Koin) {
        modules(coreModule, servicesModule)
    }
}
