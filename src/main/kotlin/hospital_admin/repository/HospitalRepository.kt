package com.example.hospital_admin.repository

import com.example.domain.Hospital
import com.example.response.BaseResponse
import com.example.response.ListResponse


/**
 *
 * Repozitorijum za upravljanje podacima o Bolnicama(Tabela HospitalTable)
 *
 *
 * Ovaj repozitorijum definiše operacije koje rade sa podacima o bolnicama i
 * vraćaju odgovor u vidu [BaseResponse] ili [ListResponse]
 *
 *
 */
interface HospitalRepository {
    /**
     * Funckija za dodavanje nove bolnice
     *
     * @param hospital Bolnica za dodavanje
     * @return [BaseResponse] koji sadrži [Hospital] objekat
     */
    suspend fun addHospital(hospital: Hospital): BaseResponse<Hospital>

    /**
     * Funkcija za pronalazak svih bolnica
     *
     * @return [ListResponse] sa listom [Hospital] objekata
     */
    suspend fun getHospitals(): ListResponse<Hospital>
}