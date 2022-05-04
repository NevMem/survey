package com.nevmem.survey.project

import com.nevmem.survey.user.UserEntity

data class ProjectEntity(
    val id: Long,
    val name: String,
    val owner: UserEntity,
)
