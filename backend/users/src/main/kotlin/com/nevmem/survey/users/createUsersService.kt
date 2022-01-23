package com.nevmem.survey.service.users

import com.nevmem.survey.users.UsersService
import com.nevmem.survey.users.internal.UsersServiceImpl

fun createUsersService(): UsersService = UsersServiceImpl()
