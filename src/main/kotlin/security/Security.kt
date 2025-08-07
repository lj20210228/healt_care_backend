package com.example.security

import com.example.security.JwtConfig
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
import org.jetbrains.exposed.sql.*


/**
 * Konfiguriše JWT autentikaciju za Ktor aplikaciju.
 *
 * Ova funkcija inicijalizuje [JwtConfig] sa tajnim ključem
 * i instalira autentikacioni mehanizam u Ktor koristeći JWT.
 *
 * Korisnički ID se iz tokena dobija preko claim-a [JwtConfig.CLAIM]
 * i postavlja kao [UserIdPrincipal] koji se kasnije koristi u autentifikovanim rutama.
 *
 * @receiver [Application] na koju se primenjuje bezbednosna konfiguracija.
 */
fun Application.configureSecurity(){
    // Inicijalizacija JWT konfiguracije sa tajnim ključem

    JwtConfig.initialize("healt_care")
    install(Authentication){
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val claim=it.payload.getClaim(JwtConfig.CLAIM).asString()
                if(claim!=null){
                    UserIdPrincipal(claim)
                }else{
                    null
                }
            }
        }
    }

}