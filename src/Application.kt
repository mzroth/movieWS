package com.mzroth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.jackson.*
import io.ktor.features.*
import kotlinx.coroutines.withTimeout

val BASE_QUERY = "https://api.themoviedb.org/3/search/movie?api_key=944cbf5f14beff1c6148c442b1f2d77c&include_adult=false&query="

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/movies") {
            call.request.queryParameters["search"]?.let {
                val client = HttpClient(OkHttp)
                try {
                    withTimeout(5000) {
                        val response: String = client.get(BASE_QUERY + it)
                        client.close()
                        call.respond(HttpStatusCode.OK, response)
                    }
                } catch (timeout: Throwable) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("readable_message" to "Internal processes timed out"))
                }
            } ?: call.respond(HttpStatusCode.BadRequest, mapOf("readable_message" to "Please provide a search query parameter"))
        }
    }
}

