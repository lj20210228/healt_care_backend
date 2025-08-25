package com.example.doctor

import com.example.doctor.repository.DoctorRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.param
import io.ktor.server.routing.patch


/**
 * Rute za prisput podacima o lekarima
 *  Ove rute omogućavaju autentifikovanim korisnicima da dohvate ili izmene podatke o lekarima.
 *
 *
 * @param repository Repozitorijum gde se nalaze metode za rukovanje podacima o lekarima
 *
 */
fun Route.doctorRoutes(repository: DoctorRepository){

    authenticate {

        /**
         * GET ruta za dohvatanje podataka o svim lekarima iz jedne bolnice
         *
         *
         * @param hospitalId ID bolnice čije lekare tražimo
         * @return Statusni kod HTTP i lista lekara
         * @response 200 Lista lekara uspešno pronađena
         * @response 401 Ako korisnik nije autentifikovan
         */

        get ("/doctors/all/{hospitalId}"){
            val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@get call.respond(HttpStatusCode.Unauthorized)
            val param=call.parameters["hospitalId"]
            val result=repository.getDoctorsInHospital(param)
            call.respond(status = HttpStatusCode.fromValue(result.statusCode),result)

        }
        /**
         * GET ruta za dohvatanje podataka o svim opšte prakse lekarima iz jedne bolnice
         *
         *
         * @param hospitalId ID bolnice čije lekare tražimo
         * @return Statusni kod HTTP i lista lekara
         * @response 200 Lista lekara uspešno pronađena
         * @response 401 Ako korisnik nije autentifikovan
         */
        get("/doctors/general/all/{hospitalId}") {
            val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@get call.respond(HttpStatusCode.Unauthorized)
            val param=call.parameters["hospitalId"]
            val result=repository.getGeneralDoctorsInHospital(param)
            call.respond(status = HttpStatusCode.fromValue(result.statusCode),result)
        }
        /**
         * GET ruta za dohvatanje podataka o lekarima neke specijalizacije iz jedne bolnice
         *
         *
         * @param hospitalId ID bolnice čije lekare tražimo
         * @param specialization Specijalizacija lekara koju tražimo
         * @return Statusni kod HTTP i lista lekara date specijalizacije
         * @response 200 Lista lekara uspešno pronađena
         * @response 401 Ako korisnik nije autentifikovan
         */
        get("/doctors/specialization/{hospitalId}/{specialization}") {
            val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@get call.respond(HttpStatusCode.Unauthorized)
            val param=call.parameters["hospitalId"]?: return@get call.respond(HttpStatusCode.BadRequest, "Missing hospitalId")
            val param2=call.parameters["specialization"]?: return@get call.respond(HttpStatusCode.BadRequest, "Missing specialization")
            println("Hospital id $param, $param2")
            val result=repository.getDoctorsForSpecialization(hospitalId = param, specialization =  param2)
            call.respond(status = HttpStatusCode.fromValue(result.statusCode),result)
        }
        /**
         *
         * PATCH Metoda za izmenu podataka o lekaru, šalju se podaci koje treba izmeniti
         *
         * @param [EditDoctorRequest] Podaci koji treba da se izmene
         * @return Statusni kod HTTP i podaci o lekaru
         * @response 200 Podaci o lekaru uspešno ažurirani
         * @response 401 Ako korisnik nije autentifikovan
         *
         *
         */
        patch("/doctors/edit") {
            val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@patch call.respond(HttpStatusCode.Unauthorized)
            val params=call.receive<EditDoctorRequest>()
            val result=repository.editDoctorProfile(params)
            call.respond(status = HttpStatusCode.fromValue(result.statusCode),result)

        }
        /**
         *
         * PATCH Metoda za izmenu podataka o broju pacijenata kojima je lekar izabran, kada on odobri zahtev pacijenta
         *
         * @param [EditDoctorRequest] Podaci koji treba da se izmene
         * @return Statusni kod HTTP i podaci o lekaru
         * @response 200 Podaci o lekaru uspešno ažurirani
         * @response 401 Ako korisnik nije autentifikovan
         *
         *
         */
        patch("/doctors/edit/currentPatients/{doctorId}") {
            val principal=call.principal<UserIdPrincipal>()
            val userEmail=principal?.name?:return@patch call.respond(HttpStatusCode.Unauthorized)
            val param=call.parameters["doctorId"]
            val result=repository.editCurrentPatients(param)
            call.respond(status = HttpStatusCode.fromValue(result.statusCode),result)



        }
    }



}