package com.nevmem.survey.task

import com.nevmem.survey.media.MediaEntity

interface TaskEntity

data class ExportDataTaskEntity(
    val id: Long,
    val state: TaskStateEntity,
    val surveyId: Long,
    val log: List<TaskLogEntity>,
    val outputs: List<MediaEntity>,
) : TaskEntity
