package com.example.hospital_admin.service

import com.example.domain.Hospital


/**
 * Interfejs za podatke o bolnicama
 *
 *
 */
interface HospitalService {

    /**
     * Dodavanje nove bolnice od strane administratora
     *
     * @param hospital Podaci o bolnici koje treba dodati
     * @return [Hospital] Server vraća podatke o dodatoj bolnici
     */

    suspend fun addHospital(hospital: Hospital): Hospital?

    /**
     * Pronalaženje svih bolnica u sistemu
     *
     * @return [List[Hospital]] Server vraća podatke o svim bolnicama u sistemu
     */
    suspend fun getHospitals():List<Hospital?>
}