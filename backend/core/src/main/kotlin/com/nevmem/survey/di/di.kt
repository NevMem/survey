package com.nevmem.survey.di

import com.nevmem.survey.converters.convertersModule
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.RoleSerializer
import com.nevmem.survey.role.RoleSerializerImpl
import com.nevmem.survey.role.mainRoleModel
import com.nevmem.survey.service.answer.AnswersService
import com.nevmem.survey.service.answer.internal.AnswersServiceImpl
import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.service.fs.FileSystemService
import com.nevmem.survey.service.fs.internal.FileSystemServiceImpl
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.service.auth.createTokenService
import com.nevmem.survey.service.media.MediaStorageService
import com.nevmem.survey.service.media.internal.MediaStorageServiceImpl
import com.nevmem.survey.auth.PasswordEncoder
import com.nevmem.survey.auth.createPasswordEncoder
import com.nevmem.survey.invites.createInvitesService
import com.nevmem.survey.service.surveys.SurveysMetadataAssembler
import com.nevmem.survey.service.surveys.SurveysService
import com.nevmem.survey.service.surveys.internal.SurveysMetadataAssembleImpl
import com.nevmem.survey.service.surveys.internal.SurveysServiceImpl
import com.nevmem.survey.users.UsersService
import com.nevmem.survey.service.users.createUsersService
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

private val coreModule = module {
    single<RoleModel> { mainRoleModel() }
    single<RoleSerializer> { RoleSerializerImpl(get()) }
    single<TokenService> { createTokenService() }
    single<PasswordEncoder> { createPasswordEncoder() }
    single<InvitesService> { createInvitesService() }
    single<SurveysService> { SurveysServiceImpl() }
    single<FileSystemService> { FileSystemServiceImpl() }
    single<MediaStorageService> { MediaStorageServiceImpl() }
    single<AnswersService> { AnswersServiceImpl() }
    single<SurveysMetadataAssembler> { SurveysMetadataAssembleImpl() }
    single<UsersService> { createUsersService() }
}

fun Application.di() {
    install(Koin) {
        modules(
            coreModule,
            convertersModule,
        )
    }
}
