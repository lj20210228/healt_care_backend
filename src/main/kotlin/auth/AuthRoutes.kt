package com.example.auth

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * Definise rute za registraciju i prijavu korisnika
 */


fun Route.authRoutes(){


    post("/auth/login") {


    }
    post("/auth/register") {

    }
}