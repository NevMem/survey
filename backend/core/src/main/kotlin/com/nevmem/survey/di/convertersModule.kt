package com.nevmem.survey.converters

import com.nevmem.surveys.converters.CommonQuestionsConverter
import com.nevmem.surveys.converters.InvitesConverter
import com.nevmem.surveys.converters.MediaConverter
import com.nevmem.surveys.converters.MediaGalleryConverter
import com.nevmem.surveys.converters.QuestionsConverter
import com.nevmem.surveys.converters.RolesConverter
import com.nevmem.surveys.converters.SurveysConverter
import com.nevmem.surveys.converters.UsersConverter
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
