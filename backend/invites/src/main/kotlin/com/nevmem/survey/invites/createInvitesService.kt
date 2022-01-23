package com.nevmem.survey.invites

import com.nevmem.survey.invites.internal.InvitesServiceImpl
import com.nevmem.survey.invites.internal.InvitesTable
import org.jetbrains.exposed.sql.Table

fun createInvitesService(): InvitesService = InvitesServiceImpl()

fun invitesTables(): List<Table> = listOf(InvitesTable)
