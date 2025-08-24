package com.example.hospital_admin.repository

import com.example.domain.Hospital
import com.example.hospital_admin.service.HospitalService
import com.example.response.BaseResponse
import com.example.response.ListResponse


/**
 * Repozitorijum koji implementira HospitalRepository interfejs
 *
 *
 * @param service Servis koji služi za komunikaciju sa bazom podataka,
 * rukuje operacijama vezanim za HospitalTable
 */
class HospitalRepositoryImplementation(val service: HospitalService): HospitalRepository {

    /**
     * Funkcija za dodavanje nove bolnice
     *
     * @param hospital Bolnica koja se dodaje
     * @return [BaseResponse] koji ako je odgovor na zahtev uspešan vraća podatke o [Hospital],
     * u suprotnom vraća poruku o grešci
     */
    override suspend fun addHospital(hospital: Hospital): BaseResponse<Hospital> {
        val result=service.addHospital(hospital)
       return if (result!=null){
           BaseResponse.SuccessResponse(data = hospital, message = "Bolnica uspešno dodata")
        }else BaseResponse.ErrorResponse(message = "Bolnica nije dodata")
    }

    /**
     * Funkcija za pregled svih bolnica
     *
     * @return [BaseResponse] ukoliko je odgovor sa server uspešan funkcija vraća
     * listu svih bolnica, u suprotnom poruku o grešci
     */
    override suspend fun getHospitals(): ListResponse<Hospital> {
        val result=service.getHospitals()
        return if (result.isEmpty())
            ListResponse.ErrorResponse(message = "Nema bolnica")
        else ListResponse.SuccessResponse(message = "Lista bolnica pronađena", data = result)
    }
}