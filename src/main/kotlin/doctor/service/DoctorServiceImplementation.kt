package com.example.doctor.service

import com.example.database.DatabaseFactory
import com.example.database.DoctorTable
import com.example.doctor.EditDoctorRequest
import com.example.domain.Doctor
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.select
import java.util.Locale

import java.util.UUID
import javax.print.Doc
import kotlin.math.max

/**
 * Klasa koja implementira metode DoctorService, koje komuniciraju za bazom podataka
 */

class DoctorServiceImplementation: DoctorService {

    /**
     * Metoda koja vraća sve lekare iz jedne bolnice, za prosleđeni id bolnice
     *
     * @param hospitalId Id bolnice
     * @return [List[Doctor]] Lista svih lekara u bolnici
     */
    override suspend fun getDoctorsInHospital(hospitalId: String?): List<Doctor?> {
        return DatabaseFactory.dbQuery {
            DoctorTable.selectAll().where {
                DoctorTable.hospitalId eq UUID.fromString(hospitalId)
            }.map {
                rowToDoctor(it)
            }
        }
    }
    /**
     * Metoda koja vraća sve lekare opšte prakse iz jedne bolnice, za prosleđeni id bolnice
     *
     * @param hospitalId Id bolnice
     * @return [List[Doctor]] Lista svih lekara opšte prakse u bolnici
     */
    override suspend fun getGeneralDoctorsInHospital(hospitalId: String?): List<Doctor?> {

        return DatabaseFactory.dbQuery {
            DoctorTable.selectAll().where{
                ( DoctorTable.hospitalId eq UUID.fromString(hospitalId)) and
                 (DoctorTable.isGeneral eq true)
            } .map {
                rowToDoctor(it)
            }
        }
    }

    /**
     * Funkcija kojom lekar ažurira svoje podatke, to jest bolnicu u kojoj radi ili maksimalan broj pacijenata
     *
     * @param request Podaci koje je potrebno ažurirati
     * @return [Doctor] Metoda vraća podatke o lekaru ili null ako lekar ne bude pronađen u bazi
     */
    override suspend fun editDoctorProfile(request: EditDoctorRequest): Doctor? {
        var doctor: ResultRow?=null
        DatabaseFactory.dbQuery {
            DoctorTable.update({DoctorTable.userId eq UUID.fromString(request.userId)}){
                if (request.maxPatients!=null){
                    it[maxPatients]=request.maxPatients
                }
                if(request.hospital!=null){
                    it[hospitalId]= UUID.fromString(request.hospital)
                }
            }
           doctor= DoctorTable.selectAll().where{
                DoctorTable.userId eq UUID.fromString(request.userId)
            }.singleOrNull()

        }
        return rowToDoctor(doctor)

    }

    /**
     * Metoda koja povećava broj trenutnih pacijenata kojima je lekar izabrani
     *
     *
     * @param id Id lekara u tabeli DoctorTable
     * @return [Doctor] Podaci o lekaru nakon ažuriranja
     */

    override suspend fun editCurrentPatients(id: String): Doctor? {
        return DatabaseFactory.dbQuery {
            DoctorTable.update({DoctorTable.id eq UUID.fromString(id)}){
                with(SqlExpressionBuilder){
                    it.update(currentPatients,currentPatients+1)
                }
            }
             DoctorTable.selectAll().where {
                 DoctorTable.id eq UUID.fromString(id)
            }.map {
                rowToDoctor(it)
             }.singleOrNull()

        }
    }

    /**
     * Funkcija koja vraća sve lekare određene specijalizacije u određenoj bolnici
     *
     * @param specialization Specijalizacije za koju tražimo lekare
     * @param hospitalId Id bolnice u kojoj tražimo lekare
     *
     */
    override suspend fun getDoctorsForSpecialization(
        specialization: String,
        hospitalId: String
    ): List<Doctor?> {

        return DatabaseFactory.dbQuery {
            DoctorTable.selectAll().where {
                DoctorTable.specialization.lowerCase() eq specialization.lowercase() and
                        (DoctorTable.hospitalId eq UUID.fromString(hospitalId))
            }.map {
                rowToDoctor(it)
            }
        }
    }


    /**
     * Funckija koja pretvara red u DoctorTable u objekat Doctor
     *
     * @param row Red u tabeli sa podacima o lekaru
     * @return [Doctor] Objekat Doctor u kome se nalaze podaci o lekaru
     */
    private fun rowToDoctor(row: ResultRow?): Doctor?{
        return if (row!=null){
            Doctor(
                id = row[DoctorTable.id].toString(),
                fullName = row[DoctorTable.fullName],
                specialization = row[DoctorTable.specialization],
                healtCareCenter = row[DoctorTable.hospitalId].toString(),
                maxPatients =row[DoctorTable.maxPatients],
                currentPatients = row[DoctorTable.currentPatients],
                isGeneral = row[DoctorTable.isGeneral],
                userId = row[DoctorTable.userId].toString()
            )
        }else null
    }


}