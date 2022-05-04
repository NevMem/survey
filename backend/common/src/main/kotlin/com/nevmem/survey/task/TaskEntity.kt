package com.nevmem.survey.task

import com.nevmem.survey.media.MediaEntity

open class TaskEntity(
    val id: Long,
    val projectId: Long,
    val log: List<TaskLogEntity>,
    val outputs: List<MediaEntity>,
)

class ExportDataTaskEntity(
    id: Long,
    projectId: Long,
    val state: TaskStateEntity,
    val surveyId: Long,
    log: List<TaskLogEntity>,
    outputs: List<MediaEntity>,
) : TaskEntity(id, projectId, log, outputs)
