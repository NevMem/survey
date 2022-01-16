package com.nevmem.survey.converter

import org.koin.dsl.module

val convertersModule = module {
    single { UsersConverter() }
    single { RolesConverter() }
    single { InvitesConverter() }
    single { QuestionsConverter() }
    single { CommonQuestionsConverter() }
    single { SurveysConverter() }
    single { MediaConverter() }
    single { MediaGalleryConverter() }
}
