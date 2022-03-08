package com.nevmem.survey.di

import com.nevmem.survey.auth.PasswordEncoder
import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.auth.createPasswordEncoder
import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.fs.FileSystemService
import com.nevmem.survey.fs.createFileSystemService
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.invites.createInvitesService
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.media.createMediaStorageService
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.RoleSerializer
import com.nevmem.survey.role.createRoleSerializer
import com.nevmem.survey.role.mainRoleModel
import com.nevmem.survey.service.auth.createTokenService
import com.nevmem.survey.survey.AnswersService
import com.nevmem.survey.survey.SurveysMetadataAssembler
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.survey.createAnswersService
import com.nevmem.survey.survey.createSurveyMetadataAssembler
import com.nevmem.survey.survey.createSurveysService
import com.nevmem.survey.users.UsersService
import com.nevmem.survey.users.createUsersService
import com.nevmem.survey.worker.api.createWorkerApi
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

private val coreModule = module {
    single<RoleModel> { mainRoleModel() }
    single<RoleSerializer> { createRoleSerializer(get()) }
    single<TokenService> { createTokenService() }
    single<PasswordEncoder> { createPasswordEncoder() }
    single<InvitesService> { createInvitesService() }
    single<SurveysService> { createSurveysService() }
    single<FileSystemService> { createFileSystemService() }
    single<MediaStorageService> { createMediaStorageService() }
    single<AnswersService> { createAnswersService() }
    single<SurveysMetadataAssembler> { createSurveyMetadataAssembler() }
    single<UsersService> { createUsersService() }
    single { createWorkerApi(EnvVars.Worker.uri!!) }
}

fun Application.di() {
    install(Koin) {
        modules(
            coreModule,
            convertersModule,
        )
    }
}
