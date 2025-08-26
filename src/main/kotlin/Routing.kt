package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.authRoutes
import com.example.auth.repository.AuthRepositoryImplementation
import com.example.auth.service.AuthServiceImplementation
import com.example.doctor.doctorRoutes
import com.example.doctor.repository.DoctorRepositoryImplementation
import com.example.doctor.service.DoctorServiceImplementation
import com.example.hospital_admin.hospitalRoutes
import com.example.hospital_admin.repository.HospitalRepositoryImplementation
import com.example.hospital_admin.service.HospitalServiceImplementation
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
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

/**
 *
 * Definise sve rute unutar aplikacije
 */


fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authRoutes(repository = AuthRepositoryImplementation(AuthServiceImplementation(
            jwtService = JwtConfig.instance
        )))
        hospitalRoutes(repository = HospitalRepositoryImplementation(HospitalServiceImplementation()))
        doctorRoutes(repository = DoctorRepositoryImplementation(DoctorServiceImplementation()))
    }
}
