package com.nevmem.survey.service.publisher

import java.util.UUID

class PublisherIdProvider {
    fun provide(): String {
        return UUID.randomUUID().toString()
    }
}
