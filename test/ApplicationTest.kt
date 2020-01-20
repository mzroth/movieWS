package com.mzroth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.jackson.*
import io.ktor.features.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/movies").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val readableError: ReadableError = jacksonObjectMapper().readValue(response.content ?: "")
                assertEquals("Please provide a search query parameter", readableError.readable_message)
            }
        }
    }
}
