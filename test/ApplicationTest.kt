package com.mzroth

import io.ktor.http.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mzroth.models.ReadableError
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

    //TODO: Finish writing the tests. Normally I would have written theses first, but as I was on a time limit I wanted to get something working to show.
}
