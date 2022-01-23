package com.nevmem.survey.service.users

import com.nevmem.survey.users.internal.UsersServiceImpl
import com.nevmem.survey.users.UsersService

fun createUsersService(): UsersService = UsersServiceImpl()
