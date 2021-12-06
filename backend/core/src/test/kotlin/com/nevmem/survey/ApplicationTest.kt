package com.nevmem.survey

import com.nevmem.survey.routing.configureRouting
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testPing() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/ping").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("pong", response.content)
            }
        }
    }
}