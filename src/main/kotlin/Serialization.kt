package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.AuthResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.jetbrains.exposed.sql.*

fun Application.configureSerialization() {

    routing {
        install(ContentNegotiation){
            json(
                Json {
                    prettyPrint=true
                    ignoreUnknownKeys=true
                    isLenient=true
                    encodeDefaults=true
                    coerceInputValues=true
                    encodeDefaults=true
                    serializersModule= SerializersModule {
                        polymorphic(
                            Any::class,
                            actualClass = AuthResponse::class,
                            actualSerializer = AuthResponse.serializer()
                        )
                    }
                }

            )
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
