package com.mzroth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*

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
                
            } ?: call.respondText("Please provide a search query parameter", status = HttpStatusCode.BadRequest)
        }
    }
}

