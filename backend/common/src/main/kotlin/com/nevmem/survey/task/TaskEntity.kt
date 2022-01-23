package com.nevmem.survey.task

interface TaskEntity

data class ExportDataTaskEntity(
    val id: Long,
    val state: TaskStateEntity,
    val surveyId: Long,
    val log: List<TaskLogEntity>,
) : TaskEntity
