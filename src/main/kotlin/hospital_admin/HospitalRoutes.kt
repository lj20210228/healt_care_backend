package com.example.hospital_admin

import com.example.domain.Hospital
import com.example.hospital_admin.repository.HospitalRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 *
 * Rute koje služe za pristup podacima o bolnici
 *
 * @param repository Repozitorijum gde se nalaze metode vezane za Hospital
 */
fun Route.hospitalRoutes( repository: HospitalRepository){
    /**
     *POST ruta za dodavanje nove bolnice, prihvata podatke iz zahteva, poziva metodu
     * na kraju prihvata odgovor sa servera
     */
    post("/hospital/add") {
        val params=call.receive<Hospital>()
        val response=repository.addHospital(params)
        call.respond(status = HttpStatusCode.fromValue(response.statusCode),response)


    }

    /**
     * GET ruta za dohvatanje svih bolnica iz baze
     *
     * -authenticate koristi JwtConfig za obradu tokena, služi za zaštitu pristupa podacima,
     *  proverava se da li postoji token, ako ne vraća grešku 401,ako da nastavlja dalje
     *
     *
     * - Šalje se zahtev serveru, pa ukoliko je uspešan vraća se lista svih bolnica
     *
     */
    authenticate {
        get("/hospital/all") {
             val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@get call.respond(HttpStatusCode.Unauthorized)
            val response=repository.getHospitals()
            call.respond(status = HttpStatusCode.fromValue(response.statusCode),response)

        }
    }

}