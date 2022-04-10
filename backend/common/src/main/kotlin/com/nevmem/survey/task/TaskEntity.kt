package com.nevmem.survey.task

import com.nevmem.survey.media.MediaEntity

open class TaskEntity(val id: Long, val log: List<TaskLogEntity>, val outputs: List<MediaEntity>)

class ExportDataTaskEntity(
    id: Long,
    val state: TaskStateEntity,
    val surveyId: Long,
    log: List<TaskLogEntity>,
    outputs: List<MediaEntity>,
) : TaskEntity(id, log, outputs)
