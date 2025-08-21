package com.example.auth

import com.example.auth.repository.AuthRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * Definiše rute za registraciju i prijavu korisnika
 * @param repository Repozitorijum gde se nalaze metode za prijavu i registraciju
 */


fun Route.authRoutes( repository: AuthRepository){


    /**
     * Ruta za prijavljivanje/logovanje
     */
    post("/auth/login") {
        val params=call.receive<LoginRequest>()
        val user=repository.loginUser(params)
        call.respond(status = HttpStatusCode.fromValue(user.statusCode),user)


    }
    /**
     * Ruta za registraciju
     */
    post("/auth/register") {
        val params=call.receive<RegisterRequest>()
        println(params)
        val user=repository.registerUser(params)
        call.respond(status = HttpStatusCode.fromValue(user.statusCode),user)
    }
}