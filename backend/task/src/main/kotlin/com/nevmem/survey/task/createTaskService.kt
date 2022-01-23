package com.nevmem.survey.task

import com.nevmem.survey.task.internal.ExportDataTaskTable
import com.nevmem.survey.task.internal.TaskLogTable
import com.nevmem.survey.task.internal.TaskServiceImpl
import org.jetbrains.exposed.sql.Table

fun createTaskService(): TaskService = TaskServiceImpl()

fun tasksTables(): List<Table> = listOf(
    ExportDataTaskTable,
    TaskLogTable,
)
