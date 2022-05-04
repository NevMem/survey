package com.nevmem.surveys.converters

import com.nevmem.survey.data.project.Project
import com.nevmem.survey.project.ProjectEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProjectConverter : KoinComponent {
    private val usersConverter by inject<UsersConverter>()

    operator fun invoke(entity: ProjectEntity): Project {
        return Project(
            id = entity.id,
            name = entity.name,
            owner = usersConverter(entity.owner)
        )
    }
}
