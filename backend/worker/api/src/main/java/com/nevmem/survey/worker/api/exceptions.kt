package com.nevmem.survey.worker.api

class TaskNotFoundException(val taskId: Long) : Exception()

class ErrorCreateTask(message: String) : Exception(message)
