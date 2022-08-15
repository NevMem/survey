package com.nevmem.survey.di

import com.nevmem.survey.auth.PasswordEncoder
import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.auth.createPasswordEncoder
import com.nevmem.survey.cloud.CloudServices
import com.nevmem.survey.config.ConfigProvider
import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.fs.FileSystemService
import com.nevmem.survey.fs.createFileSystemService
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.invites.createInvitesService
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.media.createMediaStorageService
import com.nevmem.survey.push.client.createPushClient
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.RoleSerializer
import com.nevmem.survey.role.createRoleSerializer
import com.nevmem.survey.role.mainRoleModel
import com.nevmem.survey.s3client.S3ClientWrapper
import com.nevmem.survey.service.auth.createTokenService
import com.nevmem.survey.survey.AnswersService
import com.nevmem.survey.survey.SurveysMetadataAssembler
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.survey.createAnswersService
import com.nevmem.survey.survey.createProjectsService
import com.nevmem.survey.survey.createSurveyMetadataAssembler
import com.nevmem.survey.survey.createSurveysService
import com.nevmem.survey.users.UsersService
import com.nevmem.survey.users.createUsersService
import com.nevmem.survey.worker.api.WorkerClientFactory
import io.ktor.application.Application
import io.ktor.application.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

private val coreModule = module {
    single<RoleModel> { mainRoleModel() }
    single<RoleSerializer> { createRoleSerializer(get()) }
    single<TokenService> { createTokenService() }
    single<PasswordEncoder> { createPasswordEncoder() }
    single<InvitesService> { createInvitesService(get()) }
    single<SurveysService> { createSurveysService() }
    single<FileSystemService> { createFileSystemService() }
    single<MediaStorageService> { createMediaStorageService() }
    single {
        val deps = object : S3ClientWrapper.Dependencies {
            override val region: String
                get() = EnvVars.S3.region
            override val uri: String
                get() = EnvVars.S3.s3Uri
            override val keyId: String
                get() = EnvVars.S3.accessKey
            override val secretAccessKey: String
                get() = EnvVars.S3.secretAccessKey
        }
        S3ClientWrapper.create(deps)
    }
    single { CloudServices.create() }
    single {
        val cloudServices: CloudServices = get()
        ConfigProvider.create(
            CoroutineScope(Dispatchers.Default + SupervisorJob()),
            get(),
            cloudServices.messaging,
        )
    }
    single<AnswersService> { createAnswersService(get(), CoroutineScope(Dispatchers.Default + SupervisorJob()), get()) }
    single<SurveysMetadataAssembler> { createSurveyMetadataAssembler() }
    single<UsersService> { createUsersService() }
    single { WorkerClientFactory.create(EnvVars.Worker.uri!!) }
    single { createPushClient(EnvVars.Push.uri!!) }
    single { createProjectsService(get(), get()) }
}

fun Application.di() {
    install(Koin) {
        modules(
            coreModule,
            convertersModule,
        )
    }
}
