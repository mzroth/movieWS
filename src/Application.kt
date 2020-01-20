package com.mzroth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.jackson.*
import io.ktor.features.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withTimeout

private const val BASE_QUERY = "https://api.themoviedb.org/3/search/movie?api_key=944cbf5f14beff1c6148c442b1f2d77c&include_adult=false&query="
private const val CONFIGURATION_URL = "https://api.themoviedb.org/3/configuration?api_key=944cbf5f14beff1c6148c442b1f2d77c"
private const val RESPONSE_SIZE = 10
private var IMAGE_BASE_URL = "http://image.tmdb.org/t/p/"
private const val IMAGE_SIZE = "w500"


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
                    withTimeout(10000) {
                        val response: TmdbResponse = jacksonObjectMapper().readValue(client.get<String>(BASE_QUERY + it))
                        jacksonObjectMapper().readValue<Configuration>(client.get<String>(CONFIGURATION_URL)).images.also {
                            IMAGE_BASE_URL = it.base_url
                        }
                        client.close()
                        call.respond(HttpStatusCode.OK, convertMovieResponse(response))
                    }
                } catch (timeout: Throwable) {
                    print(timeout)
                    client.cancel("Timeout", timeout)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("readable_message" to "Internal processes timed out"))
                }
            } ?: call.respond(HttpStatusCode.BadRequest, mapOf("readable_message" to "Please provide a search query parameter"))
        }
    }
}

private fun convertMovieResponse(response: TmdbResponse) = response.results.take(RESPONSE_SIZE).map {
        Movie(it.id, it.title, "${it.popularity} out of ${it.vote_count} votes", it.poster_path?.let { posterPath -> "$IMAGE_BASE_URL$IMAGE_SIZE$posterPath" })
    }

