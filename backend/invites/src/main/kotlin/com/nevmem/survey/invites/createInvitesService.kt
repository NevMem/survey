package com.nevmem.survey.invites

import com.nevmem.survey.invites.internal.InvitesServiceImpl
import com.nevmem.survey.invites.internal.InvitesTable
import com.nevmem.survey.survey.ProjectsService
import org.jetbrains.exposed.sql.Table

fun createInvitesService(projectsService: ProjectsService): InvitesService = InvitesServiceImpl(projectsService)

fun invitesTables(): List<Table> = listOf(InvitesTable)
