package com.nevmem.survey.exception

class NotFoundException(message: String? = null) : Exception(message ?: "Not found")
